package com.example.taylorswitch.data.localDatabase

import kotlinx.coroutines.flow.Flow

class OfflineTradeRepository (private val tradeDao: TradeDao) : TradeRepository {
    override fun getAllTradesStream(): Flow<List<TradePostLocal>> = tradeDao.getAllTrade()

    override fun getTradeStream(id: Int): Flow<TradePostLocal?> = tradeDao.getTrade(id)

    override suspend fun insertTrade(trade: TradePostLocal) = tradeDao.insert(trade)

    override suspend fun deleteTrade(trade: TradePostLocal) = tradeDao.delete(trade)

    override  suspend fun updateTrade(trade: TradePostLocal) = tradeDao.update(trade)
}