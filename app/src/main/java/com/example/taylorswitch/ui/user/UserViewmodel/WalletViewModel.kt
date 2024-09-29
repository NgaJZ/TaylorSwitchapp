package com.example.taylorswitch.ui.user.UserViewmodel

import android.annotation.SuppressLint

import androidx.compose.runtime.getValue
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taylorswitch.data.UserProfileUiState
import com.example.taylorswitch.data.WalletUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.firestore.core.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WalletViewModel : ViewModel() {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

//    private val _walletState = MutableStateFlow(WalletUiState())
//    var walletState: StateFlow<WalletUiState> = _walletState

    var walletState by mutableStateOf(WalletUiState())
        private set
    init {
        fetchWalletData()
    }

    @SuppressLint("RestrictedApi")
    fun fetchWalletData() {
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



    // Fetch wallet balance
    fun fetchWalletBalance() {
        val userId = firebaseAuth.currentUser?.uid ?: return

        firestore.collection("user").document(userId)
            .get()
            .addOnSuccessListener { document ->
                document?.let {
                    val balance = document.getDouble("balance") ?: 0.0
                    walletState = walletState.copy(balance = balance)
                }
            }
            .addOnFailureListener { e ->
                walletState = walletState.copy(errorMessage = e.message)
            }
    }

//    // Fetch transaction history
//    fun fetchTransactionHistory() {
//        val userId = firebaseAuth.currentUser?.uid ?: return
//
//        firestore.collection("wallets").document(userId).collection("transactions")
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                val transactions = querySnapshot.documents.mapNotNull { doc ->
//                    doc.toObject(Transaction::class.java)
//                }
//                walletState = walletState.copy(transactions = transactions)
//            }
//            .addOnFailureListener { e ->
//                walletState = walletState.copy(errorMessage = e.message)
//            }
//    }
}