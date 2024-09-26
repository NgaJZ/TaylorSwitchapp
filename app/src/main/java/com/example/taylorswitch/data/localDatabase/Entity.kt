package com.example.taylorswitch.data.localDatabase
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "auctions")
data class AuctionPostLocal (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val basePrice: Double,
    val minBid: Double,
    val endDate: String,
    val endTime: String,
    val minCall: Double,
    val highestBidder: String,
    val highestBid :Double,
    val imageUris: List<String>, // List of image URIs (local paths)
    val isUploaded: Boolean = false, // Track whether the auction is uploaded to Firestore
    val live: Boolean = false
    )
