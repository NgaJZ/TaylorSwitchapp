package com.example.taylorswitch.data

enum class ListingStage{
    Live,
    End
}

enum class BidStage{
    Success,
    Fail
}
data class Bidder(
    val name: String = "",
    val bidAmount: Double = 0.0
)

data class postRec(
    val id: Long = 0L,
    val name:String = "",
    val highestBid: Long = 0L,
    val endDate: String = "",
    val endTime: String = "",
    val timeLeft: String = "",
    val live: Boolean = false

)

data class BidUiState(
    val endDate: String = " ",
    val endTime: String = "",
    val timeLeft: String = "",
    val minCallUp: Double = 0.0,
    val startBidAmount: Double = 0.0,
    val title: String = "",
    val description: String = "",
    val poster: String = " ",
    val live: Boolean = false,
    val success: Boolean = false,
    val highestBid: Double = 0.0,
    val minCall: Double = 0.0,
    val highestBidder: Bidder = Bidder("",0.0),
    val historyBidder: List<Bidder> = emptyList(),
    val stage: ListingStage = ListingStage.Live,
    val postRecArr: List<postRec> = emptyList(),
    val imageRef: List<String> = emptyList()
)
