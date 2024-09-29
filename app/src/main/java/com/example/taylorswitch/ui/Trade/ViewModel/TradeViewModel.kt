package com.example.taylorswitch.ui.Trade.ViewModel

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taylorswitch.data.Listing
import com.example.taylorswitch.data.TradeStatus
import com.example.taylorswitch.data.TradeUiState
import com.example.taylorswitch.data.fireStore.model.Trade
import com.example.taylorswitch.data.tradeHistory
import com.example.taylorswitch.util.StorageUtil
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.lang.reflect.Array

class TradeViewModel : ViewModel() {
    private val _tUiState = MutableStateFlow(TradeUiState())
    val tUiState: StateFlow<TradeUiState> = _tUiState.asStateFlow()

    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var category by mutableStateOf("")
    var imageRef: List<String> = emptyList()
    var trader by mutableStateOf("")
    var tradeItem: List<String> = emptyList()
    var tradeItemUris by mutableStateOf<List<Uri>>(emptyList())
    var tradeStatus: TradeStatus = TradeStatus.Pending
    var live by mutableStateOf(false)

    private var _tradeList = MutableStateFlow<List<Trade>>(emptyList())
    var tradeList = _tradeList.asStateFlow()
    private val db = Firebase.firestore

    private var _trade = MutableStateFlow<Trade?>(null)
    var trade = _trade.asStateFlow()

    var imageUris by mutableStateOf<List<Uri>>(emptyList())
    var postImageUris by mutableStateOf<List<Uri>>(emptyList())
    var tradingUiState by mutableStateOf(TradingUiState())
        private set

    var uid by mutableStateOf("")
    var username by mutableStateOf("")
    var owner by mutableStateOf("")
    var ownerName by mutableStateOf("")
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val firebaseState = FirebaseStorage.getInstance()
    val userFireStorePath = db.collection("user").document("0").collection("UserTradeRec")

    init {
        getTradeList()
    }

    fun getUserProfileByUid(uid: String){
        db.collection("user").document(uid)
            .get()
            .addOnSuccessListener { userSnapshot ->
                if(userSnapshot.exists()){
                    val ownerName = userSnapshot.getString("username") ?: ""

                    Log.d("Firestore", "Username: $ownerName")

                    _tUiState.update { currentState ->
                        currentState.copy(
                            owner = ownerName
                        )
                    }
                }else{
                    Log.e("FirestoreError", "No document found with UID: $uid")
                    _tUiState.update { currentState ->
                        currentState.copy(
                            owner = "Unknown user"
                        )
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error fetching user profile")
            }
    }

    data class TradingUiState(
        val id: Int = 0,
        val title: String = "",
        val description: String = "",
        val tradeItemUris: List<String> = emptyList(),
        val category: String = "",
        val trader: String = "",
        val imageUris: List<String> = emptyList(),
        val isUploaded: Boolean = false,
        val isOpen: Boolean = false
    )

//    fun TradingUiState.toTradePostLocal(): TradePostLocal = TradePostLocal(
//        id = id,
//        title = title,
//        description = description,
//        category = category,
//        trader = trader,
//        imageUris = imageUris,
//        isUploaded = isUploaded,
//        isOpen = isOpen
//    )

    fun getTradeList(){
        db.collection("trade")
            .addSnapshotListener{ value, error ->
                if(error != null){
                    return@addSnapshotListener
                }
                if(value != null){
                    _tradeList.value = value.toObjects()
                }
            }
    }

    fun getTradeById(_id: String){
        db.collection("trade")
            .document(_id)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                _trade.value = documentSnapshot.toObject()
                _trade.value?.let { tradeData ->
                    title = tradeData.title
                    description = tradeData.description
                    category = tradeData.category
                    imageRef = tradeData.imageRef
                    owner = tradeData.owner
                    live = tradeData.live
                }
                _tUiState.update { currentState ->
                    currentState.copy(
                        title = title,
                        description = description,
                        category = category,
                        imageRef = imageRef,
                        owner = owner,
                        live = live
                    )
                }
                getUserProfileByUid(owner)
            }
    }

    fun updateListingTitle(ListingTitle: String){
        title = ListingTitle
        _tUiState.update { currentState ->
            currentState.copy(
                title = title
            )
        }
    }

    fun updateListingDescription(ListingDescription: String){
        description = ListingDescription
        _tUiState.update { currentState ->
            currentState.copy(
                description = description
            )
        }
    }

    fun updateCategory(ListingCategory: String){
        category = ListingCategory
        _tUiState.update { currentState ->
            currentState.copy(
                category = category
            )
        }
    }

    fun updateOwner(name: String){
        owner = name
        _tUiState.update { currentState ->
            currentState.copy(
                owner = owner
            )
        }
    }
    fun updateImage(uris: List<Uri>){
        postImageUris = uris
        _tUiState.update { currentState ->
            currentState.copy(
                imageUris = postImageUris
            )
        }
    }

    fun postTrade(owner: String, context: Context): Boolean {
        getCurrentUid()
        var postSuccess: Boolean = false

        if(title != "" && postImageUris.isNotEmpty()){
            db.collection("trade").orderBy("id", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    var newTradeId = 1

                    if(!documents.isEmpty){
                        val lastDocId = documents.documents[0].id
                        newTradeId = (lastDocId.toIntOrNull() ?: 0) + 1
                    }

                    val trade = hashMapOf(
                        "id" to newTradeId,
                        "title" to title,
                        "description" to description,
                        "category" to category,
                        "live" to true,
                        "owner" to uid
                    )

                    val postReference = db.collection("trade").document(newTradeId.toString())

                    db.collection("trade").document(newTradeId.toString())
                        .set(trade, SetOptions.merge())
                        .addOnSuccessListener{
                            postImageUris.forEach { uri ->
                                uri?.let {
                                    StorageUtil.uploadToStorage(
                                        uri = it,
                                        context = context,
                                        type = "image",
                                        postReference = db.collection("trade"),
                                        postId = newTradeId.toString()
                                    )
                                }
                            }
                            db.collection("user").document(uid).collection("UserTradeRec").document("userPost")
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
        }else{
            Toast.makeText(
                context,
                "Please filled up all details",
                Toast.LENGTH_SHORT
            ).show()
        }
        return postSuccess
    }

    fun resetPosting() {
        postImageUris = emptyList()
        title = ""
        description = ""
        category = ""
        owner = ""
    }

    fun resetUiState(){
        _tUiState.update { currentState ->
            currentState.copy(
                title = "",
                description = "",
                category = "",
                owner = "",
                trader = "",
                live = false,
                stage = Listing.Open,
                tradeStatus = TradeStatus.Pending,
                tradeHistoryArr = emptyList(),
                imageRef = emptyList(),
                imageUris = emptyList()
            )
        }
    }

    fun closeListing(_trade_id: String){
        live = false
        _tUiState.update { currentState ->
            currentState.copy(
                live = live
            )
        }

        db.collection("trade").document(_trade_id)
            .update("live", false)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }

    fun getUserHistoryArray(userId: String = "0", document:String = "userPost", field:String = "postTradeRef"){
        val postList = mutableListOf<tradeHistory>()

        userFireStorePath.document(document)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if(documentSnapshot.exists()) {
                    val tradeHistoryArray = documentSnapshot.get(field) as? List<DocumentReference>

                    if(tradeHistoryArray != null) {
                        for (ref in tradeHistoryArray) {
                            ref.get().addOnSuccessListener { userSnapshot ->
                                if(userSnapshot.exists()) {
                                    val history = tradeHistory(
                                        id = userSnapshot.getLong("id") ?:0L,
                                        title = userSnapshot.getString("title") ?: "",
                                        imageRef = userSnapshot.get("imageRef") as? List<String> ?:emptyList()
                                    )
                                    postList.add(history)
                                    _tUiState.update { currentState ->
                                        currentState.copy(
                                            tradeHistoryArr = postList
                                        )
                                    }
                                }
                            }.addOnFailureListener { exception ->
                                println("Error getting referenced document: $exception")
                            }
                        }
                    }else{
                        _tUiState.update { currentState ->
                            currentState.copy(
                                tradeHistoryArr = emptyList()
                            )
                        }
                    }
                }
            }.addOnFailureListener { exception ->
                println("Error getting document: $exception")
            }
    }
    fun getUserPost(user: String = "test"){
        userFireStorePath.document("userPost")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val userReference = documentSnapshot.getDocumentReference("postTradeRef")

                userReference?.get()?.addOnSuccessListener { userSnapshot ->
                    handleUserData(userSnapshot);
                }
            }
    }
    private fun handleUserData(userSnapshot: DocumentSnapshot){
        if(userSnapshot.exists()){
            val tradeItemTitle = userSnapshot.getString("title") ?: "No Title"
            val itemDescription = userSnapshot.getString("description") ?: "No Description"
            val itemCategory = userSnapshot.getString("category") ?: "No category"

            _tUiState.update { currentState ->
                currentState.copy(
                    title = tradeItemTitle,
                    description = itemDescription,
                    category = itemCategory
                )
            }
        }else{
            Log.d("Firestore", "User document does not exist!")
        }
    }
    fun checkWinOrNot(user: String = "", tradeId: String = "0"): Boolean{
        getTradeById(tradeId)
        return user == trader
    }
    private fun updateTrade(trader: String, tradeId: String){
        getCurrentUid()
        var isOpen: Boolean = false
        db.collection("trade").document(tradeId).get()
            .addOnSuccessListener { documentSnapshot ->
                _trade.value = documentSnapshot.toObject()
                trade.value?.let { tradeData ->
                    isOpen = tradeData.live
                }
                if(isOpen){
                    _tUiState.update { currentState ->
                        currentState.copy(
                            trader = trader
                        )
                    }
                    db.collection("trade").document(tradeId).collection("trader")
                        .orderBy("tradeId", Query.Direction.DESCENDING)
                        .limit(1)
                        .get()
                        .addOnSuccessListener { documents ->
                            var newTradeId = 1

                            if(!documents.isEmpty){
                                val lastDocId = documents.documents[0].id
                                newTradeId = (lastDocId.toIntOrNull() ?: 0) + 1
                            }
                            val traderR = hashMapOf(
                                "tradeId" to newTradeId,
                                "name" to trader,
                                "trade item" to tradeItem
                            )

                            val tradeReference =db.collection("trade").document(tradeId)
                            tradeReference
                                .set("tradeArray" to FieldValue.arrayUnion(traderR))
                                .addOnSuccessListener {
                                    tradeReference
                                        .set(
                                            mapOf(
                                                "trader" to trader,
                                                "trade item" to tradeItem
                                            ), SetOptions.merge()
                                        )
                                        .addOnSuccessListener {
                                            db.collection("trade").document(uid)
                                                .collection("UserTradeRec")
                                                .document("userTrade")
                                                .set(
                                                    hashMapOf(
                                                        "tradeRef" to FieldValue.arrayUnion(tradeReference)
                                                    ), SetOptions.merge()
                                                )
                                        }
                                    Log.d("document", "CREATED")
                                }
                        }
                }
            }
    }
    fun isCallNotValid(): Boolean {
        val tradeCall = tradeItem
        if (tradeCall.isNotEmpty()){
            return true
        }
        return false
    }
    fun callTrade(tradeId: String){
        getCurrentUid()
        val tradeCall = tradeItem
        if(isCallNotValid()){
            if(owner != uid){
            updateTrade(trader, tradeId = tradeId)
            }
        }
    }
    fun rejectTrade(tradeId: String){
        _tUiState.update { currentState ->
            currentState.copy(
                tradeStatus = TradeStatus.Rejected
            )
        }
        db.collection("trade").document(tradeId)
            .update("trade status", "Rejected")
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }
    fun acceptTrade(tradeId: String){
        _tUiState.update { currentState ->
            currentState.copy(
                tradeStatus = TradeStatus.Accepted
            )
        }
        db.collection("trade").document(tradeId)
            .update("trade status", "Accepted")
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }
    private fun getCurrentUid(){
        uid = firebaseAuth.currentUser?.uid ?: return
    }
}