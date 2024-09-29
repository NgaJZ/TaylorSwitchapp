package com.example.taylorswitch

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.taylorswitch.ui.theme.TaylorSwitchTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import com.example.taylorswitch.ui.Auction.UiScreen.BidHistoryScreen
import com.example.taylorswitch.ui.Auction.UiScreen.BidSession

import com.example.taylorswitch.ui.Auction.Viewmodel.BidViewModel
import com.example.taylorswitch.ui.Auction.UiScreen.HomeScreen
import com.example.taylorswitch.ui.Auction.UiScreen.PostHistoryScreen
import com.example.taylorswitch.ui.Auction.UiScreen.PostScreen
import com.example.taylorswitch.ui.user.UserViewmodel.UserLoginViewModel
import com.example.taylorswitch.ui.user.UserViewmodel.UserViewModel
import com.example.taylorswitch.ui.user.login.LoginScreen
import com.example.taylorswitch.ui.user.signup.SignUpScreen
import com.example.taylorswitch.ui.theme.AppViewModelProvider
import com.example.taylorswitch.ui.user.UserViewmodel.TopUpViewModel
import com.example.taylorswitch.ui.user.UserViewmodel.UserProfileViewModel
import com.example.taylorswitch.ui.user.UserViewmodel.WalletViewModel
import com.example.taylorswitch.ui.user.profile.EditProfileScreen
import com.example.taylorswitch.ui.user.profile.TopUpScreen
import com.example.taylorswitch.ui.user.profile.WalletScreen
import kotlinx.coroutines.launch

enum class TaylorSwitchScreen() {
    LoginPage,
    SignUpPage,
    EditProfilePage,
    WalletPage,
    TopUpScreen,
    MainPage,
    ViewBid,
    PostBid,
    BidHistory,
    PostHistory,
    Test
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaylorSwitchApp(
    viewModel: BidViewModel = viewModel(),
    userLoginViewModel: UserLoginViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    userProfileViewModel: UserProfileViewModel = viewModel(),
    walletViewModel: WalletViewModel = viewModel(),
    topUpViewModel: TopUpViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Drawer title", modifier = Modifier.padding(16.dp))
                HorizontalDivider()
                NavigationDrawerItem(
                    label = {
                        Text(text = "Bid History")
                    },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                        navController.navigate(TaylorSwitchScreen.BidHistory.name)

                    }
                )
                NavigationDrawerItem(
                    label = {
                        Text(text = "Post History")
                    },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                        navController.navigate(TaylorSwitchScreen.PostHistory.name)

                    }
                )
                NavigationDrawerItem(
                    label = {
                        Text(text = "Post Bid")
                    },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                        navController.navigate(TaylorSwitchScreen.PostBid.name)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("main") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                        navController.navigate(TaylorSwitchScreen.MainPage.name)
                    }
                )

                NavigationDrawerItem(
                    label = { Text("test") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                        navController.navigate(TaylorSwitchScreen.Test.name)
                    }
                )

                NavigationDrawerItem(
                    label = { Text("login") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                        navController.navigate(TaylorSwitchScreen.LoginPage.name)
                    }
                )                // ...other drawer items

                NavigationDrawerItem(
                    label = { Text("Edit Profile") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                        navController.navigate(TaylorSwitchScreen.EditProfilePage.name)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("My Wallet") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                        navController.navigate(TaylorSwitchScreen.WalletPage.name)
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TaylorSwitchAppBar(
                    canNavigateBack = false,
                    navigateUp = { /* TODO: implement back navigation */ },
                    onNavigationIconClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            }
        )
        { innerPadding ->
            val uiState by viewModel.uiState.collectAsState()
            NavHost(
                navController = navController,
                startDestination = TaylorSwitchScreen.Test.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(TaylorSwitchScreen.MainPage.name) {
                    HomeScreen(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
                //Bid
                composable(route = TaylorSwitchScreen.PostBid.name) {
//                    viewModel.resetUiState()
                    PostScreen(
                        onPostButtonClicked = {
                            viewModel.postBid(context = context)
                        },
                        bidViewModel = viewModel
                    )
                }

                composable(TaylorSwitchScreen.ViewBid.name + "/{auctionId}") { backStackEntry ->
                    val auctionId = backStackEntry.arguments?.getString("auctionId")
                    viewModel.getAuctionById("$auctionId")
                     BidSession(
                        auctionId = auctionId,
                        bidUiState = uiState,
                        bidViewModel = viewModel
                    )
                }

                composable(TaylorSwitchScreen.PostHistory.name) {

//                    viewModel.getUserHistoryArray("0", "userPost","postRef")
                    PostHistoryScreen(
                        bidViewModel = viewModel,
                        list = uiState.historyRecArr,
                        navController = navController

                    )
                }
                composable(TaylorSwitchScreen.BidHistory.name) {
//                    viewModel.getUserHistoryArray("0", "userBid","bidRef")
                    BidHistoryScreen(
                        bidViewModel = viewModel,
                        list = uiState.historyRecArr,
                        navController = navController

                    )
                }
                composable(TaylorSwitchScreen.Test.name) {
                    MultiplePhotoPicker(navController = navController, viewModel = userLoginViewModel)
                }
                composable(TaylorSwitchScreen.LoginPage.name){
                    LoginScreen(
                        viewModel =  userLoginViewModel,
                        navController = navController,
                        onSignUpClick = {
                            navController.navigate(TaylorSwitchScreen.SignUpPage.name)
                        }
//                        onForgotPasswordClick= {},  // Function to handle "Forgot Password" click
//                        onSignUpClick = {}         // Function to handle "Sign Up" navigation
                    )
                }
                composable(TaylorSwitchScreen.SignUpPage.name){
                    SignUpScreen(
                        viewModel =  userViewModel,
                        navController = navController,
//                        onForgotPasswordClick= {},  // Function to handle "Forgot Password" click
//                        onSignUpClick = {}         // Function to handle "Sign Up" navigation
                    )
                }
                composable(TaylorSwitchScreen.EditProfilePage.name){
                    EditProfileScreen(
                        viewModel =  userProfileViewModel,
                        onBackClick = {navController.navigate(TaylorSwitchScreen.MainPage.name)},
                        navController = navController
//                        onForgotPasswordClick= {},  // Function to handle "Forgot Password" click
//                        onSignUpClick = {}         // Function to handle "Sign Up" navigation
                    )
                }
                composable(TaylorSwitchScreen.WalletPage.name){
                    WalletScreen(
                        viewModel =  walletViewModel,
                        //onBackClick = {navController.navigate(TaylorSwitchScreen.MainPage.name)},
                        navController = navController,
//                        onForgotPasswordClick= {},  // Function to handle "Forgot Password" click
                        onTopUpClick = {navController.navigate(TaylorSwitchScreen.TopUpScreen.name)}         // Function to handle "Sign Up" navigation
                    )
                }
                composable(TaylorSwitchScreen.WalletPage.name){
                    TopUpScreen(
                        viewModel =  topUpViewModel,
                        //currentBalance = topUpViewModel.topUp(currentBalance).toString(),
                        //onBackClick = {navController.navigate(TaylorSwitchScreen.MainPage.name)},
                        navController = navController
//                        onForgotPasswordClick= {},  // Function to handle "Forgot Password" click
//                        onSignUpClick = {}         // Function to handle "Sign Up" navigation
                    )
                }
            }
        }
    }
}


@Preview(showSystemUi = false, showBackground = false)
@Composable
fun GreetingPreview() {
    TaylorSwitchTheme {
//        TaylorSwitchApp()
    }
}

