package com.example.taylorswitch.data.localDatabase

import kotlinx.coroutines.flow.Flow

class OfflineAuctionsRepository(private val auctionDao: AuctionDao) : AuctionsRepository {

    override fun getAllAuctionsStream(): Flow<List<AuctionPostLocal>> = auctionDao.getAllAuctions()

    override fun getAuctionStream(id: Int): Flow<AuctionPostLocal?> = auctionDao.getAuction(id)

    override suspend fun insertAuction(auction: AuctionPostLocal) = auctionDao.insert(auction)

    override suspend fun deleteAuction(auction: AuctionPostLocal) = auctionDao.delete(auction)

    override  suspend fun updateAuction(auction: AuctionPostLocal) = auctionDao.update(auction)
}