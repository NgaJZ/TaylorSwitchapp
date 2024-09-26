@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.taylorswitch.ui.Auction.Viewmodel

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taylorswitch.data.BidUiState
import com.example.taylorswitch.data.Bidder
import com.example.taylorswitch.data.ListingStage
import com.example.taylorswitch.data.fireStore.model.Auction
import com.example.taylorswitch.data.localDatabase.AuctionPostLocal
import com.example.taylorswitch.data.historyRec
import com.example.taylorswitch.util.StorageUtil
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.getField
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class BidViewModel
//    (private val auctionsRepository: AuctionsRepository)
    : ViewModel() {

    private val _uiState = MutableStateFlow(BidUiState())
    val uiState: StateFlow<BidUiState> = _uiState.asStateFlow()

    var name by mutableStateOf("")
    var description by mutableStateOf("")
    var startAmount by mutableStateOf("")
    var minAmount by mutableStateOf("")
    var poster by mutableStateOf("")
    var endDate by mutableStateOf("")
    var endTime by mutableStateOf("")

    //    var _imageUrl = MutableStateFlow<List<String>>(emptyList())
//    var imageUrl = _imageUrl.asStateFlow()
    var imageRef: List<String> = emptyList()

    //    var endTimeStamp by mutableStateOf("")
    var highestBidder by mutableStateOf(Bidder())
    var highest = highestBidder.bidAmount
    val min = minAmount.toDoubleOrNull() ?: 0.0
    var minCallAmount by mutableStateOf("")
    var bidCallAmount by mutableStateOf("")

    var timeLeft by mutableStateOf("")

    private var _auctionList = MutableStateFlow<List<Auction>>(emptyList())
    var auctionList = _auctionList.asStateFlow()
    private val db = Firebase.firestore

    private var _auction = MutableStateFlow<Auction?>(null)
    var auction = _auction.asStateFlow()

    //store image to firebase storage
    var imageUris by mutableStateOf<List<Uri>>(emptyList())

    var auctionUiState by mutableStateOf(AuctionUiState())
        private set

    val userFirestorePath = db.collection("user").document("0").collection("userBidRec")

    init {
//        getAuctionById()
        getAuctionList()
    }


    data class AuctionUiState (
        val id: Int = 0,
        val name: String= "",
        val description: String = "",
        val basePrice: Double = 0.0,
        val minBid: Double = 0.0,
        val endDate: String = "",
        val endTime: String = "",
        val minCall: Double = 0.0,
        val highestBidder: String = "",
        val highestBid :Double = 0.0,
        val imageUris: List<String> = emptyList(), // List of image URIs (local paths)
        val isUploaded: Boolean = false, // Track whether the auction is uploaded to Firestore
        val live: Boolean = false
    )

    fun AuctionUiState.toAuctionPostLocal(): AuctionPostLocal = AuctionPostLocal(
       id =id,
        name = name,
        description  = description,
        basePrice = basePrice,
        minBid = minBid,
        endDate = endDate,
        endTime = endTime,
        minCall = minCall,
        highestBidder = highestBidder,
        highestBid = highestBid,
        imageUris = imageUris,
        isUploaded = isUploaded,
        live = live
    )


//    suspend fun saveAuction(){
//        auctionsRepository.insertAuction(auctionUiState.toAuctionPostLocal())
//    }
    fun getAuctionList() {
        db.collection("auction")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (value != null) {
                    _auctionList.value = value.toObjects()
                }
            }
    }

    fun getAuctionById(_id: String) {

        db.collection("auction")
            .document(_id)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                _auction.value = documentSnapshot.toObject()
            }

        _auction.value?.let { auctionData ->
            name = auctionData.name
            description = auctionData.description
            startAmount = (auctionData.basePrice).toString()
            minAmount = (auctionData.minBid).toString()
            minCallAmount = (auctionData.minCall).toString()
            highest = (auctionData.highestBid)
            highestBidder = Bidder(auctionData.highestBidder,auctionData.highestBid)
            endDate = (auctionData.endDate)
            endTime = (auctionData.endTime)
            imageRef = auctionData.imageRef

//            highest = (auctionData.highest).toString()
//            endTimeStamp = (auctionData.endTimestamp).toString()
        }
        val startBidAmount = startAmount.toDoubleOrNull() ?: 0.0
        val minBidAmount = minAmount.toDoubleOrNull() ?: 0.0
        _uiState.update { currentState ->
            currentState.copy(
                title = name,
                description = description,
                startBidAmount = startBidAmount,
                minCallUp = minBidAmount,
                poster = poster,
                endDate = endDate,
                endTime = endTime,
//                endTimeStamp = endTimeStamp.toString(),
                highestBid = highest,
                minCall = highest + minBidAmount,
                imageRef = imageRef
            )

        }

    }


    fun updateListingTitle(ListingTitle: String) {
        name = ListingTitle
    }

    fun updateListingDescription(ListingDescription: String) {
        description = ListingDescription
    }


    fun updateStartBidAmount(startBidAmount: String) {
        startAmount = startBidAmount
    }

    fun updateMinBidAmount(minBidAmount: String) {
        minAmount = minBidAmount
    }

    fun updatePoster(posterName: String) {
        poster = posterName
    }

    fun updateEndDate(endDateInput: String) {
        endDate = endDateInput
    }

    fun updateEndTime(endTimeInput: String) {
        endTime = endTimeInput
    }

    fun updateHighestBidder(name: String, amount: Double) {
        highestBidder = Bidder(name, amount)
    }

    fun updateBidCall(amount: String) {
        val amountInput = amount.toDoubleOrNull() ?: 0.0
        if (amountInput >= min + highestBidder.bidAmount) {
            bidCallAmount = amount
        }
    }

    fun incBidCall() {
        val bidCall = bidCallAmount.toDoubleOrNull() ?: 0.0
        val result = bidCall.plus(1)
        updateBidCall(result.toString())
    }

    fun decBidCall() {
        val bidCall = bidCallAmount.toDoubleOrNull() ?: 0.0
        if (bidCall > 0) {
            val result = bidCall.minus(1)
            updateBidCall(result.toString())
        }

    }

    fun isCallNotValid(): Boolean {
        val bidCall = bidCallAmount.toDoubleOrNull() ?: 0.0
        val minCallAmount = minCallAmount.toDoubleOrNull() ?: 0.0
        if (bidCall >= highest + minCallAmount) {
            return false
        }
        return true
    }

    fun callBid(bidder: String = "test", auctionId: String) {
        val bidCall = bidCallAmount.toDoubleOrNull() ?: 0.0
        if (!isCallNotValid()) {

            updateBid(Bidder(name = bidder, bidAmount = bidCall), auctionId = auctionId)
//            val bidder = hashMapOf(
//                "name" to bidder,
//                "callAmount" to bidCall
//            )
//            db.collection("auction").document(auction_id.toString()).collection("bidder").document("")
//                .set(auction)
//                .addOnSuccessListener {
//                    Log.d("document", "CREATED")
//                }


//        }
        }
    }

    //    @RequiresApi(Build.VERSION_CODES.O)
//    @SuppressLint("ServiceCast")
    fun postBid(context: Context) {
        val startBidAmount = startAmount.toDoubleOrNull() ?: 0.0
        val minBidAmount = minAmount.toDoubleOrNull() ?: 0.0



        if (name != "" && startBidAmount != 0.0 && minBidAmount != 0.0 && endDate != "" && endTime != "" && imageUris.isNotEmpty()) {
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
                        "name" to name,
                        "description" to description,
                        "basePrice" to startBidAmount,
                        "minBid" to minBidAmount,
                        "endDate" to endDate,
                        "endTime" to endTime,
                        "minCall" to startBidAmount + minBidAmount,
                        "highestBidder" to "",
                        "highestBid" to 0.0,
                        "live" to true
                    )

                    val postReference = db.collection("auction").document(newAuctionId.toString())

                    // Step 2: Store the new auction with the incremented ID
                    postReference
                        .set(auction)
                        .addOnSuccessListener {
                            // Auction successfully stored
                            imageUris.forEach { uri ->

                                uri?.let {
                                    StorageUtil.uploadToStorage(
                                        uri = it,
                                        context = context,
                                        type = "image",
                                        postReference = db.collection("auction"),
                                        postId = newAuctionId.toString()
                                    )
                                }
                            }
                            userFirestorePath.document("userPost")
                                .update("postRef", FieldValue.arrayUnion(postReference))
                            Log.d("document", "CREATED")
                            resetPosting()
                            resetUiState()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        context,
                        "upload failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(
                context,
                "Please filled up all the details",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    fun resetPosting() {
        imageUris = emptyList()
        name = ""
        description = ""
        startAmount = ""
        minAmount = ""
        poster = ""
        endDate = ""
        endTime = ""
    }

    fun resetUiState() {
        _uiState.update { currentState ->
            currentState.copy(
                endDate = " ",
                endTime = "",
                timeLeft = "",
                minCallUp = 0.0,
                startBidAmount = 0.0,
                title = "",
                description = "",
                poster = " ",
                live = false,
                success = false,
                highestBid = 0.0,
                minCall = 0.0,
                highestBidder = Bidder("", 0.0),
                historyBidder = emptyList(),
                stage = ListingStage.Live,
                historyRecArr = emptyList(),
                imageRef = emptyList()
            )
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun isEndDateValid(endDate: String): Boolean {
        // Define the pattern used to parse the date-time string
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // Parse the end date string to LocalDateTime
        val endDate = LocalDateTime.parse(endDate, formatter)

        // Get the current date-time
        val currentDateTime = LocalDateTime.now()

        // Return true if the end date is not after the current date-time
        return !endDate.isAfter(currentDateTime)
    }

    fun calculateTimeLeft(targetDate: String, targetTime: String): Long {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault())
        val targetDateTimeString = "$targetDate $targetTime"
        val targetDateTime = dateFormat.parse(targetDateTimeString) ?: return 0L
        val currentDate = Date()

        // Calculate the difference in milliseconds
        return targetDateTime.time - currentDate.time
    }

    // Function to format the time left in a readable way
    fun formatTimeLeft(diffInMillis: Long): String {
        if (diffInMillis <= 0L) return "Time's up!"

        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis) % 60

        return "$hours:$minutes:$seconds"
    }


//    private fun combineDateTime(dateString: String, timeString: String): Timestamp? {
//        // Define date and time formatters
//        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
//        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
//
//        try {
//            // Parse the date and time strings into Date objects
//            val date = dateFormat.parse(dateString)
//            val time = timeFormat.parse(timeString)
//
//            if (date != null && time != null) {
//                // Use Calendar to combine date and time
//                val calendar = Calendar.getInstance()
//
//                // Set the parsed date
//                calendar.time = date
//
//                // Extract hours and minutes from the parsed time and set it on the date
//                val timeCalendar = Calendar.getInstance()
//                timeCalendar.time = time
//                calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
//                calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
//                calendar.set(Calendar.SECOND, 0)
//                calendar.set(Calendar.MILLISECOND, 0)
//
//                // Convert the combined date and time to a Firestore Timestamp
//                val combinedDate = calendar.time
//                return Timestamp(combinedDate)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        return null // Return null if there was a parsing error
//    }

    private fun updateBid(bidder: Bidder, auctionId: String) {

        getAuctionById(auctionId)

        var live: Boolean = false
        db.collection("auction").document(auctionId).get()
            .addOnSuccessListener { documentSnapshot ->
                _auction.value = documentSnapshot.toObject()
            }

        auction.value?.let { auctionData ->
            live = auctionData.live
        }

        if (live) {
            val updatedBidderList = ArrayList(_uiState.value.historyBidder)
            updatedBidderList.add(bidder)
            _uiState.update { currentState ->
                currentState.copy(
                    historyBidder = updatedBidderList,
                    highestBidder = bidder
                )
            }
            //add to bidder collection in auction collection
            db.collection("auction").document(auctionId).collection("bidder")
                .orderBy("bidId", Query.Direction.DESCENDING)
                .limit(1) // Limit to 1 to get the highest document ID
                .get()
                .addOnSuccessListener { documents ->
                    var newBidId = 1 // Default starting ID

                    // If there is at least one document, parse the last document ID and increment it
                    if (!documents.isEmpty) {
                        val lastDocId = documents.documents[0].id
                        newBidId = (lastDocId.toIntOrNull() ?: 0) + 1
                    }
                    val bidderR = hashMapOf(
                        "bidId" to newBidId,
                        "name" to bidder.name,
                        "bid amount " to bidder.bidAmount
                    )

                    val bidReference = db.collection("auction").document(auctionId)
                    val minCall = bidder.bidAmount + (minAmount.toDoubleOrNull() ?: 0.0)
                    bidReference
//                    .collection("bidder")
//                    .document(newBidId.toString())
                        .update("bidArray", FieldValue.arrayUnion(bidderR))

                        .addOnSuccessListener {
                            // Auction successfully stored
                            bidReference
                                .update(
                                    mapOf(
                                        "highestBidder" to bidder.name,
                                        "highestBid" to bidder.bidAmount,
                                        "minCall" to minCall
                                    )
                                )
                                .addOnSuccessListener {
                                    userFirestorePath.document("userBid")
                                        .update("bidRef", FieldValue.arrayUnion(bidReference))
//                                userFirestorePath.document("userBid")
//                                    .update("BidRecord", FieldValue.arrayUnion(
//                                        mapOf(
//                                            bidReference to bidder.bidAmount
//                                        )
//                                    ))

                                }
                            Log.d("document", "CREATED")
                        }
                }


//        val updatedBidderList = ArrayList(_uiState.value.historyBidder)
//        updatedBidderList.add(bidder)
//        _uiState.update{ currentState ->
//            currentState.copy(
//                historyBidder = updatedBidderList,
//                highestBidder = bidder
//            )
//        }
//    }
        }
//}
    }


    // Function to calculate the time left
//    fun calculateTimeLeft(timestampString: Timestamp): Long {
//        // Extract the seconds from the string
////        val regex = """seconds = (\d+)""".toRegex()
////        val matchResult = regex.find(timestampString)
////        val seconds = matchResult?.groups?.get(1)?.value?.toLongOrNull() ?: return 0L
//
////        val seconds = timestampString.seconds
//        // Convert seconds to milliseconds
//
//        val targetMillis = timestampString.toDate().time
//
//        // Get the current time in milliseconds
//        val currentMillis = Date().time
//
//        // Calculate the difference in milliseconds
//        return targetMillis - currentMillis
//    }
//
//    // Function to format the time left in a readable way
//    fun formatTimeLeft(diffInMillis: Long): String {
//        if (diffInMillis <= 0L) return "Time's up!"
//
//        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
//        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60
//        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis) % 60
//
//        // Format with leading zeros for better readability
//        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
//    }

    fun closeListing(_auction_id: String) {
        db.collection("auction").document(_auction_id)
            .update("live", false)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
//        _uiState.update { currentState->
//            currentState.copy(
//                live = false
//            )
//        }

    }

    fun getUserHistoryArray(userID: String = "0", document:String = "userPost", field:String = "postRef") {
        val postList = mutableListOf<historyRec>()
        // Fetch the document containing the references
        userFirestorePath.document(document)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Get the array of document references
                    val historyRefArray = documentSnapshot.get(field) as? List<DocumentReference>

                    // If the array is not null, iterate over it
                    if (historyRefArray != null) {
                        for (ref in historyRefArray) {
                            // Dereference each post and fetch the data
                            ref.get().addOnSuccessListener { userSnapshot ->
                                if (userSnapshot.exists()) {
                                    // Create postRec from the data
                                    endDate = userSnapshot.getString("endDate") ?: ""
                                    endTime = userSnapshot.getString("endTime") ?: ""

                                    val history = historyRec(
                                        id = userSnapshot.getLong("id") ?: 0L,
                                        name = userSnapshot.getString("name") ?: "",
                                        highestBid = userSnapshot.getLong("highestBid") ?: 0L,
                                        endDate = userSnapshot.getString("endDate") ?: "",
                                        endTime = userSnapshot.getString("endTime") ?: "",
                                        timeLeft = formatTimeLeft(
                                            calculateTimeLeft(
                                                endDate,
                                                endTime
                                            )
                                        ),
                                        live = userSnapshot.getBoolean("live")?: false,
                                        imageRef = userSnapshot.get("imageRef") as? List<String> ?: emptyList()
                                    )

                                    // Add postRec to the list
                                    postList.add(history)
                                    _uiState.update { currentState ->
                                        currentState.copy(
                                            historyRecArr = postList
                                        )
                                    }
                                }
                            }.addOnFailureListener { exception ->
                                println("Error getting referenced document: $exception")
                            }

                        }

                    }else{
                        _uiState.update { currentState ->
                            currentState.copy(
                                historyRecArr = emptyList()
                            )
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting document: $exception")
            }
    }

    fun getUserPost(user: String = "test") {
        userFirestorePath.document("userPost")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val userReference = documentSnapshot.getDocumentReference("postRef")

                userReference?.get()?.addOnSuccessListener { userSnapshot ->
                    // Access data from the referenced document
                    handleUserData(userSnapshot);
                }
            }
    }


    private fun handleUserData(userSnapshot: DocumentSnapshot) {
        if (userSnapshot.exists()) {
            val auctionName = userSnapshot.getString("name") ?: "No Name"
            val currentBid = userSnapshot.getDouble("highestBid") ?: 0.00
            val date = userSnapshot.getString("endDate") ?: "hh:mm am"
            val time = userSnapshot.getString("endTime") ?: "mm/dd/yyyy"
            val timeRemaining = calculateTimeLeft(date, time)
            val dateTime: String = formatTimeLeft(timeRemaining)
            // Update your UI state
            _uiState.update { currentState ->
                currentState.copy(
                    title = auctionName,
                    highestBid = currentBid,
                    timeLeft = dateTime
                )
            }
        } else {
            Log.d("Firestore", "User document does not exist!")
        }
    }


    fun checkHighestOrNot(user: String = "", auctionId: String = "0"): Boolean{
        getAuctionById(auctionId)
        return user == highestBidder.name
    }

}

//fun updateBid(){
//        val auction = hashMapOf(
//            "id" to 3,
//            "name" to title,
//            "basePrice" to startBidAmount,
//            "minBid" to minBidAmount,
//            "endTimestamp" to timeStamp,
//            "minCall" to startBidAmount + minBidAmount
//        )
//        db.collection("cars")
//            .document("")
//            .set(auction)
//            .addOnSuccessListener {
//                Log.d("document", "CREATED")
//            }
//    }
