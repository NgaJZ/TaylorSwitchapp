package com.example.taylorswitch.data.fireStore.model

import com.example.taylorswitch.data.TradeStatus

data class Trade(
    val id: Int = 0,
    val title: String,
    val description: String,
    val category: String,
    val tradeItem: List<String> = emptyList(),
    val trader: String,
    val isOpen: Boolean = false,
    val tradeStatus: TradeStatus = TradeStatus.Pending,
    val imageRef: List<String> = emptyList()
)