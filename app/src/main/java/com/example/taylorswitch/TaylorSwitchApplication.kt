package com.example.taylorswitch

import android.app.Application
import com.example.taylorswitch.data.localDatabase.AppContainer
import com.example.taylorswitch.data.localDatabase.AppDataContainer

class TaylorSwitchApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }

}