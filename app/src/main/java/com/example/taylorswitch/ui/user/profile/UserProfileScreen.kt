package com.example.taylorswitch.ui.user.profile

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import coil.compose.rememberImagePainter
import com.example.taylorswitch.ui.user.UserViewmodel.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: UserProfileViewModel,
    onBackClick: () -> Unit,
    navController: NavController
) {

    LaunchedEffect(Unit) {
        viewModel.fetchUserProfile()
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.uploadProfileImage(it)
        }
    }
    val uiState = viewModel.uiState

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile image with upload functionality
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFFE1BEE7), CircleShape)
                    .clickable { imagePickerLauncher.launch("image/*") }, // Launch image picker
                contentAlignment = Alignment.Center
            ) {
                if (uiState.profileImageUrl.isEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Image",
                        tint = Color(0xFF7E57C2),
                        modifier = Modifier.size(50.dp)
                    )
                } else {
                    Image(
                        painter = rememberImagePainter(uiState.profileImageUrl),
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(100.dp).clip(CircleShape)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // User info fields
            EditableProfileField(
                icon = Icons.Default.Person,
                label = "User Name",
                value = uiState.username,
                onValueChange = { viewModel.onUsernameChanged(it) }
            )
            EditableProfileField(
                icon = Icons.Default.Lock,
                label = "Password",
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                isPassword = true
            )
            EditableProfileField(
                icon = Icons.Default.Phone,
                label = "Phone No",
                value = uiState.phoneNumber,
                onValueChange = { viewModel.onPhoneNumberChanged(it) }
            )
            ProfileField(icon = Icons.Default.AccountBalanceWallet, label = "Wallet", value = "RM***.**")
            EditableProfileField(
                icon = Icons.Default.Cake,
                label = "Date of Birth",
                value = uiState.dateOfBirth,
                onValueChange = { viewModel.onDateOfBirthChanged(it) }
            )
            EditableProfileField(
                icon = Icons.Default.Email,
                label = "Email",
                value = uiState.email,
                onValueChange = { viewModel.onEmailChanged(it) }
            )
            EditableProfileField(
                icon = Icons.Default.LocationOn,
                label = "Address",
                value = uiState.address,
                onValueChange = {  viewModel.onAddressChanged(it) }
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


//@Composable
//fun EditableProfileField(
//    icon: ImageVector,
//    label: String,
//    value: String,
//    onValueChange: (String) -> Unit,
//    isPassword: Boolean = false
//) {
//    var isEditing by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                imageVector = icon,
//                contentDescription = label,
//                modifier = Modifier.size(40.dp),
//                tint = Color.Black
//            )
//            Spacer(modifier = Modifier.width(16.dp))
//
//            Column(modifier = Modifier.weight(1f)) {
//                Text(text = label, fontWeight = FontWeight.Bold, fontSize = 16.sp)
//                if (isEditing) {
//                    TextField(
//                        value = value,
//                        onValueChange = onValueChange,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                } else {
//                    Text(
//                        text = value,
//                        fontSize = 14.sp,
//                        color = Color.Gray,
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                }
//            }
//            IconButton(onClick = { isEditing = !isEditing }) {
//                Icon(
//                    imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
//                    contentDescription = if (isEditing) "Save" else "Edit"
//                )
//            }
//        }
//    }
//}
@Composable
fun EditableProfileField(
    icon: ImageVector,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    var isEditing by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(40.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = label, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                if (isEditing) {
                    TextField(
                        value = value,
                        onValueChange = onValueChange,
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),  // Ensures text is visible
                        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)  // Add padding if necessary
                    )
                } else {
                    Text(
                        text = value,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            IconButton(onClick = { isEditing = !isEditing }) {
                Icon(
                    imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                    contentDescription = if (isEditing) "Save" else "Edit"
                )
            }
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

        db.collection("user").document(userId)
            .set(userMap, SetOptions.merge())
            .addOnSuccessListener {
                // Successfully saved data
            }
            .addOnFailureListener { e ->
                // Handle the error
            }
    }
}


