package com.example.taylorswitch.ui.user.UserViewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taylorswitch.data.UserProfileUiState
import com.example.taylorswitch.data.UserUiState
import com.example.taylorswitch.ui.user.profile.saveUserProfileToFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class UserProfileViewModel : ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    var uiState by mutableStateOf(UserProfileUiState())
        private set

    // Get current user ID
    private val userId: String
        get() = firebaseAuth.currentUser?.uid ?: ""

    init {
        fetchUserProfile()
    }

    // Function to fetch current user profile data from Firestore
    fun fetchUserProfile() {
        val userId = firebaseAuth.currentUser?.uid ?: return

        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                document?.let {
                    val data = document.data ?: return@addOnSuccessListener
                    uiState = uiState.copy(
                        username = data["username"] as String? ?: "",
                        email = data["email"] as String? ?: "",
                        phoneNumber = data["phoneNumber"] as String? ?: "",
                        dateOfBirth = data["dateOfBirth"] as String? ?: "",
                        address = data["address"] as String? ?: ""
                    )
                }
            }
            .addOnFailureListener { e ->
                uiState = uiState.copy(errorMessage = e.message)
            }
    }
    // Function to update the user profile in Firestore
    fun updateUserProfile(
        username: String,
        password: String,
        phoneNumber: String,
        dateOfBirth: String,
        email: String,
        address: String
    ) {
        // Update ViewModel's local state
        uiState = uiState.copy(
            username = username,
            password = password,
            phoneNumber = phoneNumber,
            dateOfBirth = dateOfBirth,
            email = email,
            address = address
        )

        // Call Firebase save function
        saveUserProfileToFirebase(
            username = username,
            password = password,
            phoneNumber = phoneNumber,
            dateOfBirth = dateOfBirth,
            email = email,
            address = address
        )
//        val userId = firebaseAuth.currentUser?.uid ?: return
//        val updates = hashMapOf(
//            "username" to username,
//            "phoneNumber" to phoneNumber,
//            "dateOfBirth" to dateOfBirth,
//            "address" to address
//        )
//        uiState = uiState.copy(isLoading = true)
//        firestore.collection("users").document(userId)
//            .update(updates as Map<String, Any>)
//            .addOnSuccessListener {
//                uiState = uiState.copy(isLoading = false, isSuccess = true)
//            }
//            .addOnFailureListener { e ->
//                uiState = uiState.copy(isLoading = false, errorMessage = e.message)
//            }
    }

    // Update Username
    fun updateUsername(newUsername: String) {
        if (userId.isNotEmpty()) {
            viewModelScope.launch {
                val userDoc = firestore.collection("users").document(userId)
                userDoc.update("username", newUsername).addOnSuccessListener {
                    // Handle success (e.g., notify UI)
                }.addOnFailureListener {
                    // Handle failure (e.g., notify UI)
                }
            }
        }
    }

    // Update Password
    fun updatePassword(newPassword: String) {
        val user = firebaseAuth.currentUser
        user?.let {
            viewModelScope.launch {
                user.updatePassword(newPassword).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Handle success
                    } else {
                        uiState = uiState.copy(errorMessage = task.exception?.message)
                    }
                }
            }
        }
    }

    // Update Phone Number
    fun updatePhoneNumber(newPhoneNumber: String) {
        if (userId.isNotEmpty()) {
            viewModelScope.launch {
                val userDoc = firestore.collection("users").document(userId)
                userDoc.update("phoneNumber", newPhoneNumber).addOnSuccessListener {
                    // Handle success
                }.addOnFailureListener {
                    // Handle failure
                }
            }
        }
    }

    // Update Date of Birth
    fun updateDateOfBirth(newDateOfBirth: String) {
        if (userId.isNotEmpty()) {
            viewModelScope.launch {
                val userDoc = firestore.collection("users").document(userId)
                userDoc.update("dateOfBirth", newDateOfBirth).addOnSuccessListener {
                    // Handle success
                }.addOnFailureListener {
                    // Handle failure
                }
            }
        }
    }

    // Update Email
    fun updateEmail(newEmail: String) {
        val user = firebaseAuth.currentUser
        user?.let {
            viewModelScope.launch {
                user.updateEmail(newEmail).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Update Firestore after successful email change
                        val userDoc = firestore.collection("users").document(userId)
                        userDoc.update("email", newEmail)
                    } else {
                        uiState = uiState.copy(errorMessage = task.exception?.message)
                    }
                }
            }
        }
    }

    // Update Address
    fun updateAddress(newAddress: String) {
        if (userId.isNotEmpty()) {
            viewModelScope.launch {
                val userDoc = firestore.collection("users").document(userId)
                userDoc.update("address", newAddress).addOnSuccessListener {
                    // Handle success
                }.addOnFailureListener {
                    // Handle failure
                }
            }
        }
    }


}
