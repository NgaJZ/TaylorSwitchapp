package com.example.taylorswitch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.taylorswitch.ui.theme.TaylorSwitchTheme

class MainActivity : ComponentActivity(){
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