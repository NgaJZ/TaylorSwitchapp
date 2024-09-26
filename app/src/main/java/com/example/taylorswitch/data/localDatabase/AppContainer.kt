package com.example.taylorswitch.data.localDatabase

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val auctionsRepository: AuctionsRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */

class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val auctionsRepository: AuctionsRepository by lazy {
        OfflineAuctionsRepository(AuctionDatabase.getDatabase(context).auctionDao())
    }
}
