package com.example.taylorswitch.ui.user.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.taylorswitch.ui.user.UserViewmodel.TopUpViewModel

@Composable //viewModel: TopUpViewModel, currentBalance: String,navController: NavController
fun TopUpScreen(viewModel: TopUpViewModel,navController: NavController) {
    val uiState = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Input for custom top-up amount
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Enter your amount")
            Text(uiState.customAmount.ifEmpty { "RM ${uiState.selectedAmount}" })
        }

        // TextField for custom input
        TextField(
            value = uiState.customAmount,
            onValueChange = { viewModel.updateCustomAmount(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter custom amount") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Predefined top-up amounts
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("RM 20", "RM 50", "RM 100", "RM 200", "RM 500").forEach { amount ->
                Button(onClick = { viewModel.updateSelectedAmount(amount) }) {
                    Text(text = amount)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Cancel and Top-up buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(//addcancel or direct back to previous page
                onClick = { navController.popBackStack() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text("Cancel", color = Color.White)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button( //viewModel.topUp(currentBalance)
                onClick = { viewModel.topUp(uiState.balance) },
                modifier = Modifier.weight(1f),
                //colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
                colors = ButtonDefaults.buttonColors(Color.Green)
            ) {
                Text("Top Up", color = Color.White)
            }
        }
    }
}