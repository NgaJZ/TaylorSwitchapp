package com.example.taylorswitch.data.localDatabase

import kotlinx.coroutines.flow.Flow

interface TradeRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllTradesStream(): Flow<List<TradePostLocal>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getTradeStream(id: Int): Flow<TradePostLocal?>

    /**
     * Insert item in the data source
     */
    suspend fun insertTrade(trade: TradePostLocal)

    /**
     * Delete item from the data source
     */
    suspend fun deleteTrade(trade: TradePostLocal)

    /**
     * Update item in the data source
     */
    suspend fun updateTrade(trade: TradePostLocal)
}