package com.example.taylorswitch.ui.user.UserViewmodel

import android.annotation.SuppressLint

import androidx.compose.runtime.getValue
import android.net.Uri
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
    private fun fetchWalletData() {
        val userId = firebaseAuth.currentUser?.uid ?: return

        viewModelScope.launch {
            firestore.collection("wallets").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val balance = document.getDouble("balance") ?: 0.0
                        val transactions = document.get("transactionHistory") as? List<Map<String, Any>> ?: emptyList()

                        val transactionList = transactions.map {
                            com.example.taylorswitch.data.Transaction(
                                amount = it["amount"] as Double,
                                description = it["description"] as String,
                                date = it["date"] as String
                            )
                        }
                                //.value
                        walletState = WalletUiState(
                            balance = balance,
                            transactionHistory = transactionList
                        )
                    }
                }
        }
    }

    // Fetch wallet balance
    fun fetchWalletBalance() {
        val userId = firebaseAuth.currentUser?.uid ?: return

        firestore.collection("wallets").document(userId)
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