//package com.example.taylorswitch.data.localDatabase
//
//import android.content.Context
//import androidx.work.CoroutineWorker
//import androidx.work.WorkerParameters
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.flow.forEach
//
//class AuctionSyncWorker(
//    private val context: Context,
//    workerParams: WorkerParameters,
//    private val auctionsRepository: AuctionsRepository // Inject AuctionDao to access Room database
//) : CoroutineWorker(context, workerParams) {
//
//    override suspend fun doWork(): Result {
//        val auctionsToSync = auctionsRepository.getAllAuctionsStream() // Fetch unsynced auctions
//        val isOnline = isNetworkAvailable(context) // Check if the device is online
//
//        if (isOnline) {
//            auctionsToSync.forEach { auction ->
//                uploadAuctionToFirestore(auction)
//            }
//            return Result.success()
//        } else {
//            return Result.retry() // Retry when the device is offline
//        }
//    }
//
//    private suspend fun uploadAuctionToFirestore(auction: AuctionPostLocal) {
//        val db = FirebaseFirestore.getInstance()
//        db.collection("auction").add(auction)
//            .addOnSuccessListener {
//                // Mark as uploaded in Room
//                auctionsRepository.deleteAuction(auction)
//                Log.d("AuctionSyncWorker", "Auction uploaded successfully: ${auction.id}")
//            }
//            .addOnFailureListener {
//                Log.e("AuctionSyncWorker", "Failed to upload auction: ${auction.id}")
//            }
//    }
//
//    // Utility method to check network status
//    private fun isNetworkAvailable(context: Context): Boolean {
//        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val networkInfo = connectivityManager.activeNetworkInfo
//        return networkInfo != null && networkInfo.isConnected
//    }
//}
