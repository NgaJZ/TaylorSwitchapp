package com.example.taylorswitch.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.taylorswitch.data.AppUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    // Form field states
    var email by mutableStateOf("")
        private set
    var username by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set
    var address by mutableStateOf("")
        private set

    // Visibility states for password fields
    var isPasswordVisible by mutableStateOf(false)
        private set
    var isConfirmPasswordVisible by mutableStateOf(false)
        private set

    // Error messages for validation
    var emailError by mutableStateOf<String?>(null)
        private set
    var passwordError by mutableStateOf<String?>(null)
        private set
    var confirmPasswordError by mutableStateOf<String?>(null)
        private set
    var usernameError by mutableStateOf<String?>(null)
        private set

    // Form submission state
    var isLoading by mutableStateOf(false)
        private set
    var signupSuccess by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Functions to update form field values
    fun onEmailChanged(newEmail: String) {
        email = newEmail
        emailError = null
    }

    fun onUsernameChanged(newUsername: String) {
        username = newUsername
        usernameError = null
    }

    fun onPasswordChanged(newPassword: String) {
        password = newPassword
        passwordError = null
    }

    fun onConfirmPasswordChanged(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
        confirmPasswordError = null
    }

    fun onAddressChanged(newAddress: String) {
        address = newAddress
    }

    // Toggle visibility of passwords
    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    fun toggleConfirmPasswordVisibility() {
        isConfirmPasswordVisible = !isConfirmPasswordVisible
    }

    // Function to validate the form fields
    private fun validateForm(): Boolean {
        var isValid = true
        // Validate email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email address"
            isValid = false
        }
        // Validate username
        if (username.isBlank()) {
            usernameError = "Username cannot be empty"
            isValid = false
        }
        // Validate password
        if (password.length < 8) {
            passwordError = "Password must be at least 8 characters"
            isValid = false
        }
        // Validate password confirmation
        if (confirmPassword != password) {
            confirmPasswordError = "Passwords do not match"
            isValid = false
        }
        return isValid
    }

    // Function to handle form submission (sign-up)
//    fun onSignup() {
//        if (!validateForm()) return
//
//        viewModelScope.launch {
//            try {
//                isLoading = true
//                // Simulate API call for sign-up
//                // Replace with actual sign-up logic or API call
//                simulateApiSignup()
//                signupSuccess = true
//            } catch (e: Exception) {
//                errorMessage = e.localizedMessage ?: "Sign-up failed"
//            } finally {
//                isLoading = false
//            }
//        }
//    }

    // Mocking an API sign-up call
    private suspend fun simulateApiSignup() {
        // Simulate delay for API call
        kotlinx.coroutines.delay(2000)
        if (email == "test@test.com") {
            throw Exception("User already exists")
        }
    }
}
