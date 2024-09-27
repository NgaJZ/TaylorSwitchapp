package com.example.taylorswitch.ui.user.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.TextField // Make sure this is Material3 TextField
import androidx.compose.material3.OutlinedTextField // Use this if you prefer an outlined variant
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.taylorswitch.ui.user.UserViewmodel.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: UserProfileViewModel,
    onBackClick: () -> Unit,
    navController: NavController
) {
    val uiState = viewModel.uiState
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFFE1BEE7), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Image",
                    tint = Color(0xFF7E57C2),
                    modifier = Modifier.size(50.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // User info fields
            EditableProfileField(
                icon = Icons.Default.Person,
                label = "User Name",
                value = uiState.username,
                onValueChange = { viewModel.updateUsername(it) }
            )
            EditableProfileField(
                icon = Icons.Default.Lock,
                label = "Password",
                value = uiState.password,
                onValueChange = { viewModel.updatePassword(it) },
                isPassword = true
            )
            EditableProfileField(
                icon = Icons.Default.Phone,
                label = "Phone No",
                value = uiState.phoneNumber,
                onValueChange = { viewModel.updatePhoneNumber(it) }
            )
            ProfileField(icon = Icons.Default.AccountBalanceWallet, label = "Wallet", value = "RM***.**")
            EditableProfileField(
                icon = Icons.Default.Cake,
                label = "Date of Birth",
                value = uiState.dateOfBirth,
                onValueChange = { viewModel.updateDateOfBirth(it) }
            )
            EditableProfileField(
                icon = Icons.Default.Email,
                label = "Email",
                value = uiState.email,
                onValueChange = { viewModel.updateEmail(it) }
            )
            EditableProfileField(
                icon = Icons.Default.LocationOn,
                label = "Address",
                value = uiState.address,
                onValueChange = {  viewModel.updateAddress(it) }
            )


            // Save button
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.updateUserProfile(
                    username = uiState.username,
                    password = uiState.password,
                    phoneNumber = uiState.phoneNumber,
                    dateOfBirth = uiState.dateOfBirth,
                    email = uiState.email,
                    address = uiState.address
                ) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Save Changes")
            }

            // Error handling
            uiState.errorMessage?.let {
                Text(text = it, color = Color.Red, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun EditableProfileField(
    icon: ImageVector,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = label,
            modifier = Modifier.size(40.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            TextField(
                value = value,
                onValueChange = onValueChange,
                visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ProfileField(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(40.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = value, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

fun saveUserProfileToFirebase(
    username: String,
    password: String,
    phoneNumber: String,
    dateOfBirth: String,
    email: String,
    address: String
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val db = FirebaseFirestore.getInstance()

    if (userId != null) {
        val userMap = hashMapOf(
            "username" to username,
            "password" to password,
            "phoneNumber" to phoneNumber,
            "dateOfBirth" to dateOfBirth,
            "email" to email,
            "address" to address
        )

        db.collection("users").document(userId)
            .set(userMap)
            .addOnSuccessListener {
                // Successfully saved data
            }
            .addOnFailureListener { e ->
                // Handle the error
            }
    }
}


