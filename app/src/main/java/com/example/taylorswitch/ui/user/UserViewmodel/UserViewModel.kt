package com.example.taylorswitch.ui.user.UserViewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.taylorswitch.data.UserUiState
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.example.taylorswitch.data.fireStore.model.Auction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserViewModel : ViewModel(){

    // State holder for the entire UI state
    var uiState by mutableStateOf(UserUiState())
        private set
    private val db = Firebase.firestore
    // State holder for the entire UI state


//    // Form field states
//    var email by mutableStateOf("")
//        private set
//    var username by mutableStateOf("")
//        private set
//    var password by mutableStateOf("")
//        private set
//    var confirmPassword by mutableStateOf("")
//        private set
//    var address by mutableStateOf("")
//        private set

//    // Visibility states for password fields
//    var isPasswordVisible by mutableStateOf(false)
//        private set
//    var isConfirmPasswordVisible by mutableStateOf(false)
//        private set
//
//    // Error messages for validation
//    var emailError by mutableStateOf<String?>(null)
//        private set
//    var passwordError by mutableStateOf<String?>(null)
//        private set
//    var confirmPasswordError by mutableStateOf<String?>(null)
//        private set
//    var usernameError by mutableStateOf<String?>(null)
//        private set
//
//    // Form submission state
//    var isLoading by mutableStateOf(false)
//        private set
//    var signupSuccess by mutableStateOf(false)
//        private set
//    var errorMessage by mutableStateOf<String?>(null)
//        private set

    // Function to update the email field
    fun onEmailChanged(newEmail: String) {
        uiState = uiState.copy(email = newEmail, emailError = null)
    }

    // Function to update the username field
    fun onUsernameChanged(newUsername: String) {
        uiState = uiState.copy(username = newUsername, usernameError = null)
    }

    // Function to update the password field
    fun onPasswordChanged(newPassword: String) {
        uiState = uiState.copy(password = newPassword, passwordError = null)
    }

    // Function to update the confirm password field
    fun onConfirmPasswordChanged(newConfirmPassword: String) {
        uiState = uiState.copy(confirmPassword = newConfirmPassword, confirmPasswordError = null)
    }

    // Function to update the address field
    fun onAddressChanged(newAddress: String) {
        uiState = uiState.copy(address = newAddress)
    }

    // Toggle password visibility
    fun togglePasswordVisibility() {
        uiState = uiState.copy(isPasswordVisible = !uiState.isPasswordVisible)
    }

    // Toggle confirm password visibility
    fun toggleConfirmPasswordVisibility() {
        uiState = uiState.copy(isConfirmPasswordVisible = !uiState.isConfirmPasswordVisible)
    }

    // Function to validate the form fields
    private fun validateForm(): Boolean {
        var isValid = true
        var emailError: String? = null
        var usernameError: String? = null
        var passwordError: String? = null
        var confirmPasswordError: String? = null

        // Email validation
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(uiState.email).matches()) {
            emailError = "Invalid email address"
            isValid = false
        }
        // Username validation
        if (uiState.username.isBlank()) {
            usernameError = "Username cannot be empty"
            isValid = false
        }

        // Password validation
        if (uiState.password.length < 8) {
            passwordError = "Password must be at least 8 characters"
            isValid = false
        }

        // Confirm password validation
        if (uiState.confirmPassword != uiState.password) {
            confirmPasswordError = "Passwords do not match"
            isValid = false
        }

        // Update the UI state with error messages if any
        uiState = uiState.copy(
            emailError = emailError,
            usernameError = usernameError,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError
        )
        return isValid
    }

    // Handle form submission
    fun onSignup() {
        if (!validateForm()) return

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            try {
                simulateApiSignup()
                uiState = uiState.copy(signupSuccess = true)
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.localizedMessage ?: "Sign-up failed")
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    // Mock API sign-up call
    private suspend fun simulateApiSignup() {
        kotlinx.coroutines.delay(2000)
        if (uiState.email == "test@test.com") {
            throw Exception("User already exists")
        }
    }
}
