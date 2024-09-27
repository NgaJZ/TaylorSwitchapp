package com.example.taylorswitch.data.fireStore.model

data class User(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

//data class UserUiState(
//    val username: String = "",
//    val email: String = "",
//    val password: String = "",
//    val phoneNumber: String = "",
//    val dateOfBirth: String = "",
//    val address: String = "",
//    val errorMessage: String? = null,
//    val isLoading: Boolean = false,
//    val isSuccess: Boolean = false
//)