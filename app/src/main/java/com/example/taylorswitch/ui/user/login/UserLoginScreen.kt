package com.example.taylorswitch.ui.user.login


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.taylorswitch.TaylorSwitchScreen
import com.example.taylorswitch.ui.theme.TaylorSwitchTheme
import com.example.taylorswitch.ui.user.UserViewmodel.LoginNavigation
import com.example.taylorswitch.ui.user.UserViewmodel.UserLoginViewModel
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: UserLoginViewModel,
    navController: NavController,
    onSignUpClick: ()-> Unit
//    onForgotPasswordClick: () -> Unit,  // Function to handle "Forgot Password" click
    // Function to handle "Sign Up" navigation
) {

    val uiState = viewModel.uiState
    val navigationEvent by viewModel.navigationEvent.observeAsState()

    val loginState by viewModel.loginState.collectAsState()

    // Monitor loginState and navigate to the next screen on success
    LaunchedEffect(loginState) {
        if (loginState is UserLoginViewModel.LoginState.Success) {
            // Navigate to main page after successful login
            navController.navigate(TaylorSwitchScreen.BidMainPage.name)
        }
    }

    // Display an error message if login fails
    if (loginState is UserLoginViewModel.LoginState.Error) {
        val error = (loginState as UserLoginViewModel.LoginState.Error).message
        Text(error, color = Color.Red)
    }


    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign in",
                fontSize = 30.sp,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Email input
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = { Text("Email address") },
                singleLine = true,
                isError = uiState.emailError != null,
                placeholder = { Text("example@gmail.com") },
                trailingIcon = {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Email Verified")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.emailError != null) {
                Text(uiState.emailError, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Password input
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = { Text("Password") },
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
                placeholder = { Text("Enter your password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.passwordError != null) {
                Text(uiState.passwordError, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(8.dp))


            TextButton(onClick = { viewModel.onForgotPasswordClick() },
                modifier = Modifier.align(Alignment.End)) {
                Text(text = "Forgot password?")
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Login button
            Button(
                onClick = {
                    if (viewModel.validateForm()) { // Validate form before attempting to log in
                        viewModel.login(uiState.email, uiState.password) }},
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp)
                } else {
                    Text("Log In")
                }
                //Text(text = "Log in", fontSize = 18.sp)
            }
            // Show error message from login state
            if (loginState is UserLoginViewModel.LoginState.Error) {
                Text(
                    text = (loginState as UserLoginViewModel.LoginState.Error).message,
                    color = Color.Red
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onSignUpClick) {
                Text(text = "Don’t have an account? Sign up")
            }
            // Google Sign-In Button
//            Button(
//                onClick = { onGoogleSignInClick() },
//                modifier = Modifier.fillMaxWidth().padding(8.dp),
//                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
//            ) {
//                Text(text = "Sign in with Google", fontSize = 18.sp)
//            }
            // Handle login result
            when (loginState) {
                is UserLoginViewModel.LoginState.Loading -> CircularProgressIndicator()
                is UserLoginViewModel.LoginState.Error -> {
                    Text(
                        text = (loginState as UserLoginViewModel.LoginState.Error).message,
                        color = Color.Red
                    )
                }
                is UserLoginViewModel.LoginState.Success -> {
                    // Navigate to the home screen
//                    navController.navigate("home")
                }
                else -> { /* No state */ }
            }
            // Launcher for Google Sign-In Intent
//            val launcher = rememberLauncherForActivityResult(
//                contract = ActivityResultContracts.StartActivityForResult()
//            ) { result ->
//                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//                val account = task.getResult(ApiException::class.java)
//                account?.let {
//                    viewModel.handleGoogleSignInResult(it) { success, error ->
//                        if (!success && error != null) {
//                            // Show error message if sign-in failed
//                            Log.e("GoogleSignIn", "Google Sign-In failed: $error")
//                        }
//                    }
//                }
//            }

        }
    }

}

