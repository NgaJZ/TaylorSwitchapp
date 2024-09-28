package com.example.taylorswitch.ui.user.signup
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.taylorswitch.ui.theme.TaylorSwitchTheme
import com.example.taylorswitch.ui.user.UserViewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(viewModel: UserViewModel, navController: NavController) {
//    bidUiState: BidUiState,
//    userViewModel: UserViewModel = viewModel()
    val uiState = viewModel.uiState

//    var email by remember { mutableStateOf("") }
//    var username by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
    //var address by remember { mutableStateOf("") }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {// Email input
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = { Text("Email") },
                isError = uiState.emailError != null,
                placeholder = { Text("example@gmail.com") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.emailError != null) {
                Text(uiState.emailError, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))
            //Username input
            OutlinedTextField(
                value = uiState.username,
                onValueChange = { viewModel.onUsernameChanged(it) },
                label = { Text("Username") },
                isError = uiState.usernameError != null,
                placeholder = { Text("example") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.usernameError != null) {
                Text(uiState.usernameError, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))
            //Password
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = { Text("Create a password") },
                singleLine = true,
                visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                        Icon(
                            imageVector = if (uiState.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Password Visibility"
                        )
                    }
                },
                isError = uiState.passwordError != null,
                placeholder = { Text("must be 8 characters") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.passwordError != null) {
                Text(uiState.passwordError, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Confirm password input
            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChanged(it) },
                label = { Text("Confirm password") },
                singleLine = true,
                visualTransformation = if (uiState.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { viewModel.toggleConfirmPasswordVisibility() }) {
                        Icon(
                            imageVector = if (uiState.isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Confirm Password Visibility"
                        )
                    }
                },
                isError = uiState.confirmPasswordError != null,
                placeholder = { Text("repeat password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.confirmPasswordError != null) {
                Text(uiState.confirmPasswordError!!, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))



            Button(
                onClick = { viewModel.signUp(uiState.email,uiState.username, uiState.password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("Sign Up")
                }
//                Text(text = "Sign Up", fontSize = 18.sp)
            }
            // Error message
            if (uiState.errorMessage != null) {
                Text(uiState.errorMessage, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Navigate back to login
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Already have an account? Log in")
            }
        }
    }


//@Preview(showBackground = true)
//@Composable
//fun PreviewSignUpScreen() {
//    MyApplicationTheme {
//        SignUpScreen()
//    }
//}