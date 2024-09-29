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
import com.example.taylorswitch.data.AppUiState
import com.example.taylorswitch.data.BidUiState
import com.example.taylorswitch.data.Bidder
import com.example.taylorswitch.data.ListingStage
import com.example.taylorswitch.data.fireStore.model.Auction
import com.example.taylorswitch.data.historyRec
import com.example.taylorswitch.data.PostUiState
import com.example.taylorswitch.data.fireStore.model.User
import com.example.taylorswitch.util.StorageUtil
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class BidViewModel
//    (private val auctionsRepository: AuctionsRepository)
    : ViewModel() {

    private val _uiState = MutableStateFlow(BidUiState())
    val uiState: StateFlow<BidUiState> = _uiState.asStateFlow()

    private val _postUiState = MutableStateFlow(PostUiState())
    val postUiState: StateFlow<PostUiState> = _postUiState.asStateFlow()

    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState: StateFlow<AppUiState> = _appUiState.asStateFlow()

    private val _posterUiState = MutableStateFlow(PostUiState())
    val posterUiState: StateFlow<PostUiState> = _posterUiState.asStateFlow()


    var name by mutableStateOf("")
    var description by mutableStateOf("")
    var startAmount by mutableStateOf("")
    var minAmount by mutableStateOf("")
    var poster by mutableStateOf("")

    var endDate by mutableStateOf("")
    var endTime by mutableStateOf("")


    var postTitle by mutableStateOf("")
    var postDesc by mutableStateOf("")
    var postStart by mutableStateOf("")
    var postMin by mutableStateOf("")
    var postPoster by mutableStateOf("")

    var postEndDate by mutableStateOf("")
    var postEndTime by mutableStateOf("")





    //    var _imageUrl = MutableStateFlow<List<String>>(emptyList())
//    var imageUrl = _imageUrl.asStateFlow()
    var imageRef: List<String> = emptyList()

    //    var endTimeStamp by mutableStateOf("")
    var highestBidder by mutableStateOf(Bidder())
    var highest = highestBidder.bidAmount
    val min = minAmount.toDoubleOrNull() ?: 0.0
    var minCallAmount by mutableStateOf("")
    var bidCallAmount by mutableStateOf("")

    var liveAuction by mutableStateOf(false)

    var timeLeft by mutableStateOf("")

    private var _auctionList = MutableStateFlow<List<Auction>>(emptyList())
    var auctionList = _auctionList.asStateFlow()
    private val db = Firebase.firestore

    private var _auction = MutableStateFlow<Auction?>(null)
    var auction = _auction.asStateFlow()

    private var _posterDB = MutableStateFlow<User?>(null)
    var posterDB = _posterDB.asStateFlow()


    //store image to firebase storage
    var imageUris by mutableStateOf<List<Uri>>(emptyList())
    var postImageUris by mutableStateOf<List<Uri>>(emptyList())

    var auctionUiState by mutableStateOf(AuctionUiState())
        private set


    var uid by mutableStateOf("")
    var username by mutableStateOf("")
    var posterName by mutableStateOf("")
    val userFirestorePath = db.collection("user").document("0").collection("userBidRec")
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()
    init {
//        getAuctionById()
//getUserProfile()
//        fetchUserProfile()
        getAuctionList()
    }

//    fun fetchUserProfile() {
//        uid = firebaseAuth.currentUser?.uid ?: return
//
//    }


    fun getUserProfileByUid(uid: String) {

        db.collection("user").document(uid)
            .get()
            .addOnSuccessListener { userSnapshot ->
                // Check if the document exists
                if (userSnapshot.exists()) {
                    // Fetch the username from Firestore
                    val posterName = userSnapshot.getString("username") ?: ""

                    Log.d("Firestore", "Username: $posterName")

                    // Update the UI state with the fetched username
                    _uiState.update { currentState ->
                        currentState.copy(
                            posterName = posterName // update with the fetched username
                        )
                    }
                } else {
                    Log.e("FirestoreError", "No document found with UID: $uid")
                    // Handle case when document doesn't exist (you might want to set a default value)
                    _uiState.update { currentState ->
                        currentState.copy(
                            posterName = "Unknown User"
                        )
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle any failure in retrieving the document
                Log.e("FirestoreError", "Error fetching user profile", exception)
            }
    }

    data class AuctionUiState(
        val id: Int = 0,
        val name: String = "",
        val description: String = "",
        val basePrice: Double = 0.0,
        val minBid: Double = 0.0,
        val endDate: String = "",
        val endTime: String = "",
        val minCall: Double = 0.0,
        val highestBidder: String = "",
        val highestBid: Double = 0.0,
        val imageUris: List<String> = emptyList(), // List of image URIs (local paths)
        val isUploaded: Boolean = false, // Track whether the auction is uploaded to Firestore
        val live: Boolean = false
    )

//    fun getUserProfile() {
//        // [START get_user_profile]
//
//            uid =  firebaseAuth.currentUser?.uid ?: return
//
//            firestore.collection("user").document(userId)
//                .get()
//                .addOnSuccessListener { document ->
//                    document?.let {
//                        val data = document.data ?: return@addOnSuccessListener
//                        uiState = uiState.copy(
//                            username = data["username"] as String? ?: "",
//                            email = data["email"] as String? ?: "",
//                            phoneNumber = data["phoneNumber"] as String? ?: "",
//                            dateOfBirth = data["dateOfBirth"] as String? ?: "",
//                            address = data["address"] as String? ?: ""
//                        )
//                    }
//                }
//                .addOnFailureListener { e ->
//                    uiState = uiState.copy(errorMessage = e.message)
//                }

//        val user = Firebase.auth.currentUser
//        user?.let {
//            // Name, email address, and profile photo Url
//            username = it.displayName.toString()
//            val email = it.email
////            photoUrl = it.photoUrl
//
//            // Check if user's email is verified
//            val emailVerified = it.isEmailVerified
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getIdToken() instead.
//            uid = it.uid
//        }
//        _appUiState.update { currentContext->
//            currentContext.copy(
//                username = username
//            )
//        }


        // [END get_user_profile]
//    }






//    fun resetViewModel(){
//        name = ""
//        description = ""
//        startAmount = ""
//        minAmount = ""
//        poster = ""
//        endDate = ""
//        endTime = ""
//    }

//    fun AuctionUiState.toAuctionPostLocal(): AuctionPostLocal = AuctionPostLocal(
//       id =id,
//        name = name,
//        description  = description,
//        basePrice = basePrice,
//        minBid = minBid,
//        endDate = endDate,
//        endTime = endTime,
//        minCall = minCall,
//        highestBidder = highestBidder,
//        highestBid = highestBid,
//        imageUris = imageUris,
//        isUploaded = isUploaded,
//        live = live
//    )


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

                _auction.value?.let { auctionData ->
                    name = auctionData.name
                    description = auctionData.description
                    startAmount = (auctionData.basePrice).toString()
                    minAmount = (auctionData.minBid).toString()
                    minCallAmount = (auctionData.minCall).toString()
                    highest = (auctionData.highestBid)
                    highestBidder = Bidder(auctionData.highestBidder, auctionData.highestBid)
                    endDate = (auctionData.endDate)
                    endTime = (auctionData.endTime)
                    imageRef = auctionData.imageRef
                    bidCallAmount = auctionData.highestBid.toString()
                    liveAuction = auctionData.live
                    poster = (auctionData.poster)
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
                        imageRef = imageRef,
                        highestBidder = highestBidder,
                        live = liveAuction
                    )
                }
                getUserProfileByUid(poster)
            }
    }


    fun updateListingTitle(ListingTitle: String) {
        postTitle = ListingTitle
        _postUiState.update { currentState ->
            currentState.copy(
                title = postTitle
            )
        }
    }

    fun updateListingDescription(ListingDescription: String) {
        postDesc = ListingDescription
        _postUiState.update { currentState ->
            currentState.copy(
                description = postDesc
            )
        }
    }


    fun updateStartBidAmount(startBidAmount: String) {
        postStart = startBidAmount
        _postUiState.update { currentState ->
            currentState.copy(
                startBidInput = postStart
            )
        }
    }

    fun updateMinBidAmount(minBidAmount: String) {
        postMin = minBidAmount
        _postUiState.update { currentState ->
            currentState.copy(
                minBidInput = postMin
            )
        }
    }

    fun updatePoster(posterName: String) {
        postPoster = posterName
        _uiState.update { currentState ->
            currentState.copy(
                poster = postPoster
            )
        }
    }

    fun updateEndDate(endDateInput: String) {
        postEndDate = endDateInput
        _postUiState.update { currentState ->
            currentState.copy(
                endDate = postEndDate
            )
        }
    }

    fun updateEndTime(endTimeInput: String) {
        postEndTime = endTimeInput
        _postUiState.update { currentState ->
            currentState.copy(
                endTime = postEndTime
            )
        }
    }

    fun updateHighestBidder(name: String, amount: Double) {
        highestBidder = Bidder(name, amount)
        _uiState.update{currentState ->
            currentState.copy(
                highestBidder = highestBidder
            )
        }
    }

    fun updateBidCall(amount: String) {
        val amountInput = amount.toDoubleOrNull() ?: 0.0
        if (amountInput >= min + (highestBidder.bidAmount)) {
            bidCallAmount = amount
            _uiState.update {currentState ->
                currentState.copy(callAmount = bidCallAmount)
            }
        }

    }

 fun updateImage(uris: List<Uri>){
     postImageUris = uris
     _postUiState.update {currentState ->
         currentState.copy(
             imageUris = postImageUris
         )
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
        getCurrentUid()
        val bidCall = bidCallAmount.toDoubleOrNull() ?: 0.0
        val minBid = minAmount.toDoubleOrNull() ?: 0.0
        if (bidCall >= (highestBidder.bidAmount + minBid)) {
            return false
        }
        return true
    }

    fun callBid(auctionId: String) {
        getCurrentUid()
        val bidCall = bidCallAmount.toDoubleOrNull() ?: 0.0
        if (!isCallNotValid()) {
            if(poster != uid) {
                updateBid(Bidder(name = uid, bidAmount = bidCall), auctionId = auctionId)
            }
        }
    }

    //    @RequiresApi(Build.VERSION_CODES.O)
//    @SuppressLint("ServiceCast")
    fun postBid(poster:String, context: Context) : Boolean{
        getCurrentUid()
        val startBidAmount = postStart.toDoubleOrNull() ?: 0.0
        val minBidAmount = postMin.toDoubleOrNull() ?: 0.0

        var postSuccess : Boolean = false

        if (postTitle != "" && startBidAmount != 0.0 && minBidAmount != 0.0 && postEndDate != "" && postEndTime != "" && postImageUris.isNotEmpty()) {
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
                        "name" to postTitle,
                        "description" to postDesc,
                        "basePrice" to startBidAmount,
                        "minBid" to minBidAmount,
                        "endDate" to postEndDate,
                        "endTime" to postEndTime,
                        "minCall" to startBidAmount + minBidAmount,
                        "highestBidder" to "",
                        "highestBid" to startBidAmount,
                        "live" to true,
                        "poster" to uid
                    )

                    val postReference = db.collection("auction").document(newAuctionId.toString())

                    // Step 2: Store the new auction with the incremented ID
                    db.collection("auction").document(newAuctionId.toString())
                        .set(auction, SetOptions.merge())
                        .addOnSuccessListener {
                            // Auction successfully stored
                            postImageUris.forEach { uri ->

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
                            db.collection("user").document(uid).collection("userBidRec").document("userPost")
                                .set(hashMapOf("postRef" to FieldValue.arrayUnion(postReference)), SetOptions.merge())
                            Log.d("document", "CREATED")
                            resetPosting()
                            postSuccess = true
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
        return postSuccess
    }

//fun resetVM(){
//    imageUris = emptyList()
//    name = ""
//    description = ""
//    startAmount = ""
//    minAmount = ""
//    poster = ""
//    endDate = ""
//    endTime = ""
//}
    fun resetPosting() {
        postImageUris = emptyList()
        postTitle = ""
        postDesc = ""
        postStart = ""
        postMin = ""
        postPoster = ""
        postEndDate = ""
        postEndTime = ""
        _postUiState.update { currentState ->
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
                startBidInput = "",
                minBidInput = "",
                highestBidder = Bidder("", 0.0),
                historyBidder = emptyList(),
                stage = ListingStage.Live,
                historyRecArr = emptyList(),
                imageRef = emptyList(),
                imageUris = emptyList()
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


    private fun updateBid(bidder: Bidder, auctionId: String) {
        getCurrentUid()
        var live: Boolean = false
        db.collection("auction").document(auctionId).get()
            .addOnSuccessListener { documentSnapshot ->
                _auction.value = documentSnapshot.toObject()


                auction.value?.let { auctionData ->
                    live = auctionData.live
                }

                if (live) {
//            val updatedBidderList = ArrayList(_uiState.value.historyBidder)
//            updatedBidderList.add(bidder)
                    _uiState.update { currentState ->
                        currentState.copy(
//                    historyBidder = updatedBidderList,
                            highestBidder = bidder
                        )
                    }
                    bidCallAmount = bidder.bidAmount.toString()

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
                                .set(
                                    hashMapOf("bidArray" to FieldValue.arrayUnion(bidderR)),
                                    SetOptions.merge()
                                )
                                .addOnSuccessListener {
                                    // Auction successfully stored
                                    bidReference
                                        .set(
                                            mapOf(
                                                "highestBidder" to bidder.name,
                                                "highestBid" to bidder.bidAmount,
                                                "minCall" to minCall
                                            ), SetOptions.merge()
                                        )
                                        .addOnSuccessListener {
                                            db.collection("user").document(uid)
                                                .collection("userBidRec")
                                                .document("userBid")
                                                .set(
                                                    hashMapOf(
                                                        "bidRef" to FieldValue.arrayUnion(
                                                            bidReference
                                                        )
                                                    ), SetOptions.merge()
                                                ).addOnSuccessListener {
                                                    val updatedBidderList = ArrayList(_uiState.value.historyBidder)
                                                    updatedBidderList.add(bidder)
                                                }
                                        }
                                    Log.d("document", "CREATED")
                                }
                        }



//        _uiState.update{ currentState ->
//            currentState.copy(
//                historyBidder = updatedBidderList,
//                highestBidder = bidder
//            )
//        }
                }
            }
        }
//}



    fun closeListing(_auction_id: String) {
        liveAuction = false
        _uiState.update { currentState->
            currentState.copy(
                live = liveAuction
            )
        }

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

//    fun getUserHistoryArray(
//        document: String ,
//        field: String
//    ) {
////        getUserProfile()
//
//
//        val postList = mutableListOf<historyRec>()
//        // Fetch the document containing the references
//        db.collection("user").document(uid).collection("userBidRec")
//            .document(document)
//            .get()
//            .addOnSuccessListener { documentSnapshot ->
//                if (documentSnapshot.exists()) {
//                    // Get the array of document references
//                    val historyRefArray = documentSnapshot.get(field) as? List<DocumentReference>
//
//                    // If the array is not null, iterate over it
//                    if (historyRefArray != null) {
//                        for (ref in historyRefArray) {
////                            ref.orderBy("endDate", Query.Direction.DESCENDING)
//
//                            // Dereference each post and fetch the data
//                            ref.get().addOnSuccessListener { userSnapshot ->
//                                if (userSnapshot.exists()) {
//                                    // Create postRec from the data
//                                    endDate = userSnapshot.getString("endDate") ?: ""
//                                    endTime = userSnapshot.getString("endTime") ?: ""
//
//                                    val history = historyRec(
//                                        id = userSnapshot.getLong("id") ?: 0L,
//                                        name = userSnapshot.getString("name") ?: "",
//                                        highestBid = userSnapshot.getLong("highestBid") ?: 0L,
//                                        endDate = userSnapshot.getString("endDate") ?: "",
//                                        endTime = userSnapshot.getString("endTime") ?: "",
//                                        timeLeft = formatTimeLeft(
//                                            calculateTimeLeft(
//                                                endDate,
//                                                endTime
//                                            )
//                                        ),
//                                        live = userSnapshot.getBoolean("live") ?: false,
//                                        imageRef = userSnapshot.get("imageRef") as? List<String>
//                                            ?: emptyList()
//                                    )
//
//                                    // Add postRec to the list
//                                    postList.add(history)
//                                    _uiState.update { currentState ->
//                                        currentState.copy(
//                                            historyRecArr = postList
//                                        )
//                                    }
//                                }
//                            }.addOnFailureListener { exception ->
//                                println("Error getting referenced document: $exception")
//                            }
//
//                        }
//
//                    } else {
//                        _uiState.update { currentState ->
//                            currentState.copy(
//                                historyRecArr = emptyList()
//                            )
//                        }
//                    }
//                }
//            }
//            .addOnFailureListener { exception ->
//                println("Error getting document: $exception")
//            }
//    }

    fun getUserHistoryArray(document: String, field: String) {
        val postList = mutableListOf<historyRec>()
        getCurrentUid()
        // Fetch the document containing the references
        db.collection("user").document(uid).collection("userBidRec")
            .document(document)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Get the array of document references
                    val historyRefArray = documentSnapshot.get(field) as? List<DocumentReference>

                    if (!historyRefArray.isNullOrEmpty()) {
                        val tasks = historyRefArray.map { ref ->
                            // Fetch each referenced document
                            ref.get()
                        }

                        // When all documents are fetched
                        Tasks.whenAllComplete(tasks).addOnSuccessListener { completedTasks ->
                            completedTasks.forEach { task ->
                                // Check if the task was successful
                                if (task.isSuccessful) {
                                    val resultSnapshot = task.result as DocumentSnapshot

                                    if (resultSnapshot.exists()) {
                                        // Extract history record from each fetched document
                                        val history = historyRec(
                                            id = resultSnapshot.getLong("id") ?: 0L,
                                            name = resultSnapshot.getString("name") ?: "",
                                            highestBid = resultSnapshot.getLong("highestBid") ?: 0L,
                                            endDate = resultSnapshot.getString("endDate") ?: "",
                                            endTime = resultSnapshot.getString("endTime") ?: "",
                                            timeLeft = formatTimeLeft(
                                                calculateTimeLeft(
                                                    resultSnapshot.getString("endDate") ?: "",
                                                    resultSnapshot.getString("endTime") ?: ""
                                                )
                                            ),
                                            live = resultSnapshot.getBoolean("live") ?: false,
                                            imageRef = resultSnapshot.get("imageRef") as? List<String> ?: emptyList(),
                                            highestBidder = resultSnapshot.getString("highestBidder") ?: ""
                                        )

                                        postList.add(history)
                                    }
                                } else {
                                    // Handle failed document fetches (optional logging)
                                    Log.e("Firestore", "Error fetching document: ${task.exception}")
                                }
                            }

                            // Sort by endDate in descending order
                            postList.sortByDescending { it.endDate }
                            postList.sortByDescending { it.endTime }
                            // Update the UI state once all documents are fetched and sorted
                            _uiState.update { currentState ->
                                currentState.copy(
                                    historyRecArr = postList
                                )
                            }
                        }
                    } else {
                        // No references, set an empty list
                        _uiState.update { currentState ->
                            currentState.copy(
                                historyRecArr = emptyList()
                            )
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting document: $exception")
                _uiState.update { currentState ->
                    currentState.copy(
                        historyRecArr = emptyList()
                    )
                }
            }
    }




fun checkHighestOrNot(highestBidder: String): Boolean {
    getCurrentUid()
    return uid == highestBidder
}

    private fun getCurrentUid(){
        uid = firebaseAuth.currentUser?.uid ?: return
    }


}
