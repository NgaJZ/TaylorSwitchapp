package com.example.taylorswitch.ui.user.UserViewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.taylorswitch.data.TopUpUiState
import com.example.taylorswitch.data.WalletUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class TopUpViewModel : ViewModel() {
    var uiState by mutableStateOf(TopUpUiState())
        private set
    var walletState by mutableStateOf(WalletUiState())
        private set

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    init {
        // Fetch the current balance from Firebase when the ViewModel is initialized
        fetchCurrentBalance()
    }

    private fun fetchCurrentBalance() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance().collection("user").document(uid)
            .get()
            .addOnSuccessListener { document ->
                val currentBalance = document.getDouble("balance") ?: 0.0
                // Store the balance in the UI state
                uiState = uiState.copy(balance = currentBalance)
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error fetching balance: ", exception)
            }
    }
    // Update selected amount from predefined buttons
    fun updateSelectedAmount(amount: String) {
        uiState = uiState.copy(selectedAmount = amount, customAmount = "")
    }

    // Update custom amount entered by the user
    fun updateCustomAmount(newAmount: String) {
        uiState = uiState.copy(customAmount = newAmount)
    }

    // Top-up function to update wallet balance
    fun topUp(navController: NavController) {
        val topUpAmount = if (uiState.customAmount.isNotEmpty()) {
            uiState.customAmount.toDouble()
        } else {
            uiState.selectedAmount.replace("RM ", "").toDouble()
        }

        val newBalance = uiState.balance + topUpAmount
        updateBalanceAndAddTransactionInFirebase(newBalance, topUpAmount, navController)
    }
//    fun   topUp(currentBalance: String) {
//        val topUpAmount = if (uiState.customAmount.isNotEmpty()) uiState.customAmount else uiState.selectedAmount
//        val newBalance = currentBalance.toDouble() + topUpAmount.toDouble()
//
//        uiState = uiState.copy(balance = newBalance.toString())
//
//        // Update Firebase with the new balance
//        updateBalanceInFirebase(newBalance)
//    }

    // Function to update the wallet balance in Firebase
    private fun updateBalanceAndAddTransactionInFirebase(newBalance: Double,topUpAmount: Double, navController: NavController) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val transaction = hashMapOf(
            "amount" to topUpAmount,
            "description" to "Top-up",
            "date" to System.currentTimeMillis() // Store as timestamp
        )
        FirebaseFirestore.getInstance().collection("user").document(uid)
            .update("balance", newBalance)
            .addOnSuccessListener {
                // Add the transaction to the transactions subcollection
                FirebaseFirestore.getInstance().collection("user").document(uid)
                    .collection("transactions")
                    .add(transaction)
                    .addOnSuccessListener {
                        uiState = uiState.copy(balance = newBalance)
                        // Transaction added successfully, navigate back
                        navController.popBackStack()
                        fetchWalletData()
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Firebase", "Error adding transaction: ", exception)
                    }
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error updating balance: ", exception)
            }
    }

    @SuppressLint("RestrictedApi")
    private fun fetchWalletData() {
        val userId = firebaseAuth.currentUser?.uid ?: return

        viewModelScope.launch {
            firestore.collection("user").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val balance = document.getDouble("balance") ?: 0.0

                        // Fetch the transaction history
                        firestore.collection("user").document(userId)
                            .collection("transactions")
                            .get()
                            .addOnSuccessListener { querySnapshot ->
                                val transactions = querySnapshot.documents.map { doc ->
                                    com.example.taylorswitch.data.Transaction(
                                        amount = doc.getDouble("amount") ?: 0.0,
                                        description = doc.getString("description") ?: "",
                                        date = doc.getLong("date").toString() // Convert timestamp to string
                                    )
                                }

                                walletState = WalletUiState(
                                    balance = balance,
                                    transactionHistory = transactions
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.e("Firebase", "Error fetching transactions: ", e)
                            }
                    }
                }
        }
    }
}


