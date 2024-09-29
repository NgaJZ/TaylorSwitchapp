package com.example.taylorswitch.ui.user.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.taylorswitch.data.Transaction
import com.example.taylorswitch.ui.user.UserViewmodel.WalletViewModel

@Composable
fun WalletScreen(viewModel: WalletViewModel , navController: NavController, onTopUpClick: () -> Unit) {
    // Observe the walletState from the ViewModel
//    val walletState by viewModel.walletState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display Wallet Icon
        Icon(
            imageVector = Icons.Default.AccountBalanceWallet,
            contentDescription = "Wallet Icon",
            modifier = Modifier.size(80.dp)
        )

        // Wallet Balance
        Text(
            text = "Total Balance",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "RM ${viewModel.walletState.balance}",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Top-up Button
        Button(
            onClick = onTopUpClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors( MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Top Up",
                fontSize = 18.sp
            )
        }

        // Transaction History
        Text(
            text = "History",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        if (viewModel.walletState.transactionHistory.isEmpty()) {
            Text(
                text = "No transactions available",
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(viewModel.walletState.transactionHistory) { transaction ->
                    TransactionItem(transaction)
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
//        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = transaction.description,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = transaction.date,
                    fontSize = 14.sp
                )
            }
            Text(
                text = "RM ${transaction.amount}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (transaction.amount >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}