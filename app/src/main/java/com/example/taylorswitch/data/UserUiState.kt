package com.example.taylorswitch.data

data class UserUiState(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val address: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val emailError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val signupSuccess: Boolean = false,
    val errorMessage: String? = null
)

data class UserLoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val errorMessage: String? = null
)

data class Transaction(
    val amount: Double,
    val description: String,
    val date: String
)

data class WalletUiState(
    val balance: Double = 0.0,
    val transactionHistory: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class TopUpUiState(
    val selectedAmount: String = "",
    val customAmount: String = "",
    val balance: String = ""
)

data class UserProfileUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val dateOfBirth: String = "",
    val address: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val profileImageUrl: String = ""
)