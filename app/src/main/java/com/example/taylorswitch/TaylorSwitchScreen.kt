package com.example.taylorswitch

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
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
import com.example.taylorswitch.ui.Auction.UiScreen.PostScreen
import kotlinx.coroutines.launch

enum class TaylorSwitchScreen() {
    MainPage,
    ViewBid,
    PostBid,
    BidHistory,
    Test
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaylorSwitchApp(
    viewModel: BidViewModel = viewModel(),
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
                // ...other drawer items
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
                startDestination = TaylorSwitchScreen.PostBid.name,
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
                    PostScreen(
                        onPostButtonClicked = {
                            viewModel.postBid(context = context)
                        },
                        bidViewModel = viewModel
                    )
                }

                composable(TaylorSwitchScreen.ViewBid.name + "/{auctionId}") { backStackEntry ->
                    val auctionId = backStackEntry.arguments?.getString("auctionId")
                    BidSession(
                        auctionId = auctionId,
                        bidUiState = uiState,
                        bidViewModel = viewModel
                    )
                }

                composable(TaylorSwitchScreen.BidHistory.name) {
                    viewModel.getUserPostRefArray("0")
                    BidHistoryScreen(
                        bidViewModel = viewModel,
                        list = uiState.postRecArr,
                        navController = navController

                    )
                }
                composable(TaylorSwitchScreen.Test.name) {
                    MultiplePhotoPicker()
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