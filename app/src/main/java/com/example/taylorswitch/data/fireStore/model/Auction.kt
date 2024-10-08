package com.example.taylorswitch.data.fireStore.model

import com.google.firebase.Timestamp

data class Auction(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val basePrice: Double = 0.0,
    val minBid: Double = 0.0,
    val minCall: Double = 0.0,
    val highestBid: Double = 0.0,
    val highestBidder: String = "",
    val endDate: String = "",
    val endTime: String ="",
    val live: Boolean = false,
    val imageRef: List<String> = emptyList(),
    val poster: String = ""
)
