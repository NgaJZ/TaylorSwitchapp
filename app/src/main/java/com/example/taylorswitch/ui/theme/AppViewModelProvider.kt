package com.example.taylorswitch.ui.theme

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.taylorswitch.TaylorSwitchApplication
import com.example.taylorswitch.ui.Auction.Viewmodel.BidViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Initializer for ItemEntryViewModel
        initializer {
            BidViewModel(
//                taylorSwitchApplication().container.auctionsRepository
            )
        }

    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.taylorSwitchApplication(): TaylorSwitchApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TaylorSwitchApplication)
