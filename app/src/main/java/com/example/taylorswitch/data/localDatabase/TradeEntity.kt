package com.example.taylorswitch.data.localDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "tradePost")
data class TradePostLocal (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val category: String,
    val tradeItem: String,
    val trader: String,
    val imageUris: List<String>,
    val isUploaded: Boolean = false,
    val isOpen: Boolean = false
)