package com.example.taylorswitch.ui.user.UserViewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taylorswitch.data.AppUiState
import com.example.taylorswitch.data.BidUiState
import com.example.taylorswitch.data.UserLoginUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


enum class LoginNavigation {
    LOGIN_SUCCESS,
    SIGNUP,
    FORGOT_PASSWORD
}
class UserLoginViewModel : ViewModel() {
    // State holder for login UI
    var uiState by mutableStateOf(UserLoginUiState())
        private set

    private val _navigationEvent = MutableLiveData<LoginNavigation?>()
    val navigationEvent: LiveData<LoginNavigation?> = _navigationEvent

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle) // Mutable state flow
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow() // Public immutable state flow

    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState: StateFlow<AppUiState> = _appUiState.asStateFlow()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    // Navigate to Forgot Password
    fun onForgotPasswordClick() {
        _navigationEvent.value = LoginNavigation.FORGOT_PASSWORD
    }

    // Clear the navigation event after it is handled
    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    // Function to update the email field
    fun onEmailChanged(newEmail: String) {
        uiState = uiState.copy(email = newEmail, emailError = null)
    }

    // Function to update the password field
    fun onPasswordChanged(newPassword: String) {
        uiState = uiState.copy(password = newPassword, passwordError = null)
    }

    // Toggle password visibility
    fun togglePasswordVisibility() {
        uiState = uiState.copy(isPasswordVisible = !uiState.isPasswordVisible)
    }

    // Validate form fields
    private fun validateForm(): Boolean {
        var isValid = true
        var emailError: String? = null
        var passwordError: String? = null

        // Email validation
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(uiState.email).matches()) {
            emailError = "Invalid email address"
            isValid = false
        }

        // Password validation
        if (uiState.password.isBlank()) {
            passwordError = "Password cannot be empty"
            isValid = false
        }

        // Update UI state with any error messages
        uiState = uiState.copy(
            emailError = emailError,
            passwordError = passwordError
        )

        return isValid
    }

    // Login logic with Firebase Authentication
    fun login(email: String, password: String) {
        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
//                        updateUI(user)
                        _loginState.value = LoginState.Success
                        fetchUserProfile()

                    } else {
                        _loginState.value =
                            LoginState.Error(task.exception?.message ?: "Login failed")

                    }
                }
        }
    }

    //get user
    fun fetchUserProfile() {
        val userId = firebaseAuth.currentUser?.uid ?: return

        firestore.collection("user").document(userId)
            .get()
            .addOnSuccessListener { document ->
                document?.let {
                    val data = document.data ?: return@addOnSuccessListener
                    _appUiState.update { currentState ->
                        currentState.copy(
                            uid = data["uid"] as String? ?:"",
                            username = data["username"] as String? ?: "",
                            userImage = data["profileImageUrl"] as String? ?: ""
                        )
                    }
                }
            }
    }


    // Sign out function
    fun signOut() {
        firebaseAuth.signOut()
        _loginState.value = LoginState.Idle // Reset the login state or handle navigation in UI
    }

    // Handle login action
    fun onLogin() {
        if (!validateForm()) return

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            try {
                simulateApiLogin()
                uiState = uiState.copy(loginSuccess = true)
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.localizedMessage ?: "Login failed")
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    // Mock API login call
    private suspend fun simulateApiLogin() {
        kotlinx.coroutines.delay(2000) // Simulating network call
        if (uiState.email == "test@test.com" && uiState.password == "password123") {
            // Simulate successful login
        } else {
            throw Exception("Invalid credentials")
        }
    }

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }
}