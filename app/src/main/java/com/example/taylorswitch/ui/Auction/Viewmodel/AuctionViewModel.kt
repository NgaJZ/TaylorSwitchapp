package com.example.taylorswitch.ui.Auction.Viewmodel

import androidx.lifecycle.ViewModel
import com.example.taylorswitch.data.fireStore.model.Auction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuctionViewModel: ViewModel() {

    private var _auctionList = MutableStateFlow<List<Auction>>(emptyList())
    var auctionList = _auctionList.asStateFlow()
    private val db = Firebase.firestore

    init {
        getAuctionList()
    }
    private fun getAuctionList(){
        db.collection("auction")
            .addSnapshotListener{value, error ->
                if(error != null){
                    return@addSnapshotListener
                }
                if(value != null){
                    _auctionList.value = value.toObjects()
                }
            }
    }
//    fun postAuction(){
//        val auction = hashMapOf(
//            "id" to 3,
//            "name" to "Laptop"
//        )
//        db.collection("auction")
//            .add(auction)
//            .addOnSuccessListener {
//                Log.d("document", "CREATED")
//            }
//    }



}