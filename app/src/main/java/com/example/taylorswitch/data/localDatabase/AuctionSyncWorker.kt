package com.example.taylorswitch.data.localDatabase

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.net.toUri
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.taylorswitch.util.StorageUtil
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class AuctionSyncWorker(
    private val context: Context,
    workerParams: WorkerParameters,
    private val auctionsRepository: AuctionsRepository // Inject AuctionDao to access Room database
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val auctionsToSync = auctionsRepository.getAllAuctionsStream().first() // Fetch unsynced auctions
        val isOnline = isNetworkAvailable(context) // Check if the device is online

        if (isOnline) {
            for (auction in auctionsToSync){
                uploadAuctionToFirestore(auction, context)
            }

            return Result.success()
        } else {
            return Result.retry() // Retry when the device is offline
        }
    }

    private suspend fun uploadAuctionToFirestore(auction: AuctionPostLocal, context: Context) {
//        val db = FirebaseFirestore.getInstance()
        try {
            uploadFirebase(auction, context)
            auctionsRepository.deleteAuction(auction) // Ensure this method is correctly implemented
            Log.d("AuctionSyncWorker", "Auction uploaded successfully: ${auction.id}")
        } catch (e: Exception) {
            Log.e("AuctionSyncWorker", "Failed to upload auction: ${auction.id}", e)
            throw e // Rethrow exception if needed
        }
    }

    // Utility method to check network status
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}


fun uploadFirebase(auctionPostLocal: AuctionPostLocal, context: Context){
    val db = Firebase.firestore
    db.collection("auction").orderBy("id", Query.Direction.DESCENDING)
        .limit(1) // Limit to 1 to get the highest document ID
        .get()
        .addOnSuccessListener { documents ->
            var newAuctionId = 1 // Default starting ID

            // If there is at least one document, parse the last document ID and increment it
            if (!documents.isEmpty) {
                val lastDocId = documents.documents[0].id
                newAuctionId = (lastDocId.toIntOrNull() ?: 0) + 1
            }

            val auction = hashMapOf(
                "id" to newAuctionId,
                "name" to auctionPostLocal.name,
                "description" to auctionPostLocal.description,
                "basePrice" to auctionPostLocal.basePrice,
                "minBid" to auctionPostLocal.minBid,
                "endDate" to auctionPostLocal.endDate,
                "endTime" to auctionPostLocal.endTime,
                "minCall" to auctionPostLocal.minCall,
                "highestBidder" to auctionPostLocal.highestBidder,
                "highestBid" to auctionPostLocal.highestBid,
                "live" to auctionPostLocal.live,
                "poster" to auctionPostLocal.poster
            )

            val postReference = db.collection("auction").document(newAuctionId.toString())

            // Step 2: Store the new auction with the incremented ID
            db.collection("auction").document(newAuctionId.toString())
                .set(auction, SetOptions.merge())
                .addOnSuccessListener {
                    // Auction successfully stored
                    auctionPostLocal.imageUris.forEach { uri ->

                        uri?.let {
                            StorageUtil.uploadToStorage(
                                uri = it.toUri(),
                                context = context,
                                type = "image",
                                postReference = db.collection("auction"),
                                postId = newAuctionId.toString()
                            )
                        }
                    }
                    db.collection("user").document(auctionPostLocal.poster).collection("userBidRec")
                        .document("userPost")
                        .set(
                            hashMapOf("postRef" to FieldValue.arrayUnion(postReference)),
                            SetOptions.merge()
                        )
                    Log.d("document", "CREATED")


                }

        }

}


fun scheduleAuctionSync(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<AuctionSyncWorker>(15, TimeUnit.SECONDS)
        .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "AuctionSyncWorker",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}
