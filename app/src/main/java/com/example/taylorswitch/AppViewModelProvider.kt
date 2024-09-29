package com.example.taylorswitch

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.taylorswitch.ui.Auction.Viewmodel.BidViewModel
import com.example.taylorswitch.ui.Trade.ViewModel.TradeViewModel
import com.example.taylorswitch.ui.user.UserViewmodel.TopUpViewModel
import com.example.taylorswitch.ui.user.UserViewmodel.UserLoginViewModel
import com.example.taylorswitch.ui.user.UserViewmodel.UserProfileViewModel
import com.example.taylorswitch.ui.user.UserViewmodel.UserViewModel
import com.example.taylorswitch.ui.user.UserViewmodel.WalletViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Initializer for ItemEntryViewModel
        initializer {
            BidViewModel(
                taylorSwitchApplication().container.auctionsRepository
            )
        }
        initializer {
            TradeViewModel()
        }
        initializer {
            UserLoginViewModel()
        }
        initializer {
            UserProfileViewModel()
        }
        initializer {
            UserViewModel()
        }
        initializer {
            TopUpViewModel()
        }
        initializer {
            WalletViewModel()
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.taylorSwitchApplication(): TaylorSwitchApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TaylorSwitchApplication)
