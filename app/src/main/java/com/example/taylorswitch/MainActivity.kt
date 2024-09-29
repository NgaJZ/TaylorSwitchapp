package com.example.taylorswitch

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taylorswitch.ui.theme.TaylorSwitchTheme
import com.example.taylorswitch.ui.user.UserViewmodel.UserLoginViewModel
import com.example.taylorswitch.ui.user.login.LoginScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class MainActivity : ComponentActivity(){

    private lateinit var googleSignInClient: GoogleSignInClient
    private val userLoginViewModel: UserLoginViewModel by viewModels()
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?){
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent{
            TaylorSwitchTheme{
                TaylorSwitchApp()
//                TimePickerExamples()
//                DrawerDemo()
//                DialWithDialogExample({},{})

            }
        }
//        // Configure Google Sign-In
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id)) // Use your actual client ID here
//            .requestEmail()
//            .build()
//
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//
//        // Handle Google Sign-In request
//        userLoginViewModel.googleSignInRequest.observe(this) {
//            signInWithGoogle()
//        }
    }




}

