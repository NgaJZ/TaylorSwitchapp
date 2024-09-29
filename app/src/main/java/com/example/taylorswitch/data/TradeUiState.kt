package com.example.taylorswitch.data

import android.net.Uri

enum class Listing {
    Open,
    Closed
}
enum class TradeStatus{
    Accepted,
    Rejected,
    Pending
}

data class tradeHistory(
    val id: Long = 0L,
    val title:String = "",
    val live: Boolean = false,
    val imageRef: List<String> = emptyList()
)

data class TradeUiState(
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val owner: String = "",
    val trader: String = "",
    val tradeItem: List<String> = emptyList(),
    val live: Boolean = false,
    val stage: Listing = Listing.Open,
    val tradeStatus: TradeStatus = TradeStatus.Pending,
    val tradeHistoryArr: List<tradeHistory> = emptyList(),
    val imageRef: List<String> = emptyList(),
    val imageUris: List<Uri> = emptyList()
)