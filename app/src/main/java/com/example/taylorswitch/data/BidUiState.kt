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

data class historyRec(
    val id: Long = 0L,
    val name:String = "",
    val highestBid: Long = 0L,
    val endDate: String = "",
    val endTime: String = "",
    val timeLeft: String = "",
    val live: Boolean = false,
    val imageRef: List<String> = emptyList(),
    val highestBidder: String = ""

)

//data class bidRec(
//    val highestBidder: Bidder = Bidder("", 0.0)
//    val endDate: String = "",
//    val endTime: String = "",
//    val timeLeft: String = "",
//    val live: Boolean = false
//)


data class BidUiState(
    val endDate: String = "",
    val endTime: String = "",
    val timeLeft: String = "",
    val minCallUp: Double = 0.0,
    val startBidAmount: Double = 0.0,
    val minBidAmount:Double = 0.0,
    val startBidInput: String = "",
    val minBidInput:String = "",
    val title: String = "",
    val description: String = "",
    val poster: String = "",
    val live: Boolean = false,
    val success: Boolean = false,
    val highestBid: Double = 0.0,
    val minCall: Double = 0.0,
    val highestBidder: Bidder = Bidder("",0.0),
    val historyBidder: List<Bidder> = emptyList(),
    val stage: ListingStage = ListingStage.Live,
    val historyRecArr: List<historyRec> = emptyList(),
    val imageRef: List<String> = emptyList(),
    val posterName: String = ""
)


data class PostUiState(
    val endDate: String = " ",
    val endTime: String = "",
    val timeLeft: String = "",
    val minCallUp: Double = 0.0,
    val startBidAmount: Double = 0.0,
    val minBidAmount:Double = 0.0,
    val startBidInput: String = "",
    val minBidInput:String = "",
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
    val historyRecArr: List<historyRec> = emptyList(),
    val imageRef: List<String> = emptyList(),
)

data class PosterUiState(
    val username: String = ""
)