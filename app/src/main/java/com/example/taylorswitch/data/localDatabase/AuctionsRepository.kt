package com.example.taylorswitch.data.localDatabase

import kotlinx.coroutines.flow.Flow

interface AuctionsRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllAuctionsStream(): Flow<List<AuctionPostLocal>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getAuctionStream(id: Int): Flow<AuctionPostLocal?>

    /**
     * Insert item in the data source
     */
    suspend fun insertAuction(auction: AuctionPostLocal)

    /**
     * Delete item from the data source
     */
    suspend fun deleteAuction(auction: AuctionPostLocal)

    /**
     * Update item in the data source
     */
    suspend fun updateAuction(auction: AuctionPostLocal)

}