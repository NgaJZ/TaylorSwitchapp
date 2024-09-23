package com.example.taylorswitch.data

data class AppUiState(
    val endDate: String = "",
    val minBidAmount: Double = 0.0,
    val startBidAmount: Double = 0.0,
    val title: String = "",
    val description: String = " ",
    val poster: String = " ",
    val live: Boolean = false,
    val success: Boolean = false,
    val highestBidder: Bidder = Bidder("",0.0),
    val historyBidder: List<Bidder> = emptyList(),
    val stage: ListingStage = ListingStage.Live

)
