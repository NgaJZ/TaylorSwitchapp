package com.example.taylorswitch.ui.user.UserViewmodel

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.navigation.NavController
import com.example.taylorswitch.data.fireStore.model.Auction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserViewModel : ViewModel(){
    //private lateinit var auth: FirebaseAuth
    // State holder for the entire UI state
    var uiState by mutableStateOf(UserUiState())
        private set

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    private val _signupState = mutableStateOf<SignupState>(SignupState.Idle)
    val signupState = _signupState


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


    // Validate form before sign-up
    private fun validateForm(): Boolean {
        var isValid = true
        var emailError: String? = null
        var passwordError: String? = null
        var confirmPasswordError: String? = null

        // Validate email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(uiState.email).matches()) {
            emailError = "Invalid email"
            isValid = false
        }

        // Validate password
        if (uiState.password.length < 8) {
            passwordError = "Password must be at least 8 characters"
            isValid = false
        }

        // Confirm password validation
        if (uiState.confirmPassword != uiState.password) {
            confirmPasswordError = "Passwords don't match"
            isValid = false
        }

        // Update UI state with errors if needed
        uiState = uiState.copy(
            emailError = emailError,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError
        )

        return isValid
    }

    // Sign-up logic with Firebase Authentication
    fun signUp(email: String,username: String, password: String,navController: NavController) {
        if (!validateForm()) return

        _signupState.value = SignupState.Loading

        viewModelScope.launch {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Get the UID of the newly created user
                        val userId = firebaseAuth.currentUser?.uid ?: ""
                        // Add user to Firestore (optional)
                        val user = hashMapOf(
                            "email" to email,
                            "username" to username,
                            "password" to password
                        )
                        db.collection("user").document(userId)
                            .set(user)
                            .addOnSuccessListener {
                                Log.d("Firestore", "User added successfully!")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Firestore", "Error adding user", e)
                            }
                        //navController.navigate(Tal)
                        _signupState.value = SignupState.Success
                    } else {
                        // Handle error
                        _signupState.value = SignupState.Error(task.exception?.message ?: "Sign-up failed")
                    }
                }
        }
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

    sealed class SignupState {
        object Idle : SignupState()
        object Loading : SignupState()
        object Success : SignupState()
        data class Error(val message: String) : SignupState()
    }
}
