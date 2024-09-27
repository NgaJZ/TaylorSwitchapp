package com.example.taylorswitch.data

enum class Listing {
    Open,
    Closed
}
enum class TradeStatus{
    Accepted,
    Rejected,
    Pending
}

data class Trader(
    val name: String = "",
    val tradeItem: String = ""
)

data class tradeHistory(
    val id: Long = 0L,
    val title:String = "",
    val tradeEnd: Boolean = false,
    val isOpen: Boolean = false,
    val win: Boolean = false,
    val imageRef: List<String> = emptyList()
)

data class TradeUiState(
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val owner: String = "",
    val tradeItem: List<String> = emptyList(),
    val trader: Trader = Trader("", ""),
    val historyTrader: List<Trader> = emptyList(),
    val isOpen: Boolean = false,
    val win:Boolean = false,
    val tradeEnd: Boolean = false,
    val stage: Listing = Listing.Open,
    val tradeStatus: TradeStatus = TradeStatus.Pending,
    val tradeHistoryArr: List<tradeHistory> = emptyList(),
    val imageRef: List<String> = emptyList()
)