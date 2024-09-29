package com.example.taylorswitch.data.localDatabase

import androidx.lifecycle.LifecycleOwner
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taylorswitch.data.TradeStatus

@Entity (tableName = "tradePost")
data class TradePostLocal (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val category: String,
    val imageUris: List<String>,
    val live: Boolean = false,
    val owner: String = ""
)