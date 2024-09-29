package com.example.taylorswitch

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.taylorswitch.data.localDatabase.scheduleAuctionSync
import com.example.taylorswitch.ui.theme.TaylorSwitchTheme

class MainActivity : ComponentActivity(){
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?){
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent{
            TaylorSwitchTheme{
                TaylorSwitchApp()
            }
        }
    }
}

