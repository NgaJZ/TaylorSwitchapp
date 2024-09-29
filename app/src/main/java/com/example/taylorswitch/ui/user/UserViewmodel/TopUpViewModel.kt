package com.example.taylorswitch.ui.user.UserViewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taylorswitch.data.TopUpUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TopUpViewModel : ViewModel() {
    var uiState by mutableStateOf(TopUpUiState())
        private set

    // Update selected amount from predefined buttons
    fun updateSelectedAmount(amount: String) {
        uiState = uiState.copy(selectedAmount = amount)
    }

    // Update custom amount entered by the user
    fun updateCustomAmount(amount: String) {
        uiState = uiState.copy(customAmount = amount)
    }

    // Top-up function to update wallet balance
    fun   topUp(currentBalance: String) {
        val topUpAmount = if (uiState.customAmount.isNotEmpty()) uiState.customAmount else uiState.selectedAmount
        val newBalance = currentBalance.toDouble() + topUpAmount.toDouble()

        uiState = uiState.copy(balance = newBalance.toString())

        // Update Firebase with the new balance
        updateBalanceInFirebase(newBalance)
    }

    // Function to update the wallet balance in Firebase
    private fun updateBalanceInFirebase(newBalance: Double) {
        // Assuming current user is authenticated and UID is available
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance().collection("wallets").document(uid)
            .update("balance", newBalance)
            .addOnSuccessListener {
                // Handle successful balance update
            }
            .addOnFailureListener {
                // Handle error
            }
    }
}