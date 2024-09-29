package com.example.taylorswitch

import android.app.Application
import com.example.taylorswitch.data.localDatabase.AppContainer
import com.example.taylorswitch.data.localDatabase.AppDataContainer
import com.example.taylorswitch.data.localDatabase.scheduleAuctionSync

class TaylorSwitchApplication: Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)

    }

}