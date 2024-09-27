package com.example.taylorswitch.data.localDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TradeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trade: TradePostLocal)

    @Update
    suspend fun update(trade: TradePostLocal)

    @Delete
    suspend fun delete(trade: TradePostLocal)

    @Query("SELECT * from tradePost WHERE id = :id")
    fun getTrade(id: Int): Flow<TradePostLocal>

    @Query("SELECT * from tradePost ORDER BY title ASC")
    fun getAllTrade(): Flow<List<TradePostLocal>>
}