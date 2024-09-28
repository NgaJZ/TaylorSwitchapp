package com.example.taylorswitch

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.taylorswitch.ui.theme.TaylorSwitchTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.taylorswitch.data.MiniFabItems
import com.example.taylorswitch.data.TabBarItem
import com.example.taylorswitch.data.TopBarItem
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
import kotlinx.coroutines.launch

enum class TaylorSwitchScreen() {
    LoginPage,
    SignUpPage,
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
    viewModel: BidViewModel = viewModel(
//        factory = AppViewModelProvider.Factory
    ),
    userLoginViewModel: UserLoginViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    userProfileViewModel: UserProfileViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    //bottom app bar icon

    val tradeTab = TabBarItem(
        title = "Trade",
        path = TaylorSwitchScreen.PostBid.name,
        selectedIcon = Icons.Filled.SyncAlt,
        unselectedIcon = Icons.Outlined.SyncAlt
    )
    val bidTab = TabBarItem(
        title = "Bid",
        path = TaylorSwitchScreen.MainPage.name,
        selectedIcon = Icons.Filled.Gavel,
        unselectedIcon = Icons.Outlined.Gavel
    )

    val bidSessionTB = TopBarItem(
        canNavigateBack = true,
        title = "Bid Session"
    )

    val bidMainPageTB = TopBarItem(
        canNavigateBack = false,
        title = "Taylor Switch"
    )


//    val histor




    // creating a list of all the tabs
    val tabBarItems = listOf(tradeTab, bidTab)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
            ) {
                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_launcher_foreground),
                            contentDescription = "user"
                        )
                        Text(text = viewModel.username, fontSize = 15.sp)
                    }
                }
                Column() {

                    Text("Bid History", modifier = Modifier.padding(16.dp))
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = {
                            Text(text = "Bid Posting")
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
                            Text(text = "Bid Record")
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
            }
        }
    ) {
        Scaffold(
            topBar = {
                if(currentDestination == TaylorSwitchScreen.ViewBid.name+ "/{auctionId}"){
                    TaylorSwitchAppBar(
                        canNavigateBack = true,
                        navigateUp = { navController.popBackStack()},
                        onNavigationIconClick = {}
                    )
                }else {
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
            },
            bottomBar = {
                if (currentDestination == TaylorSwitchScreen.MainPage.name) {
                    TaylorSwitchBottomBar(tabBarItems = tabBarItems, navController = navController)
                }
            },
            floatingActionButton = { MainUI() }
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
//                    fab()
                }

                composable(TaylorSwitchScreen.ViewBid.name + "/{auctionId}") { backStackEntry ->
                    val auctionId = backStackEntry.arguments?.getString("auctionId")
                    viewModel.getAuctionById("$auctionId")
                    BidSession(
                        auctionId = auctionId,
                        bidUiState = uiState,
                        bidViewModel = viewModel,
                        navController = navController
                    )
                }

                composable(TaylorSwitchScreen.PostHistory.name) {

                    viewModel.getUserHistoryArray("userPost", "postRef")
                    PostHistoryScreen(
                        bidViewModel = viewModel,
                        list = uiState.historyRecArr,
                        navController = navController

                    )
                }
                composable(TaylorSwitchScreen.BidHistory.name) {
                    viewModel.getUserHistoryArray("userBid", "bidRef")
                    BidHistoryScreen(
                        bidViewModel = viewModel,
                        list = uiState.historyRecArr,
                        navController = navController

                    )
                }
                composable(TaylorSwitchScreen.Test.name) {
                    MultiplePhotoPicker()
                }
                composable(TaylorSwitchScreen.LoginPage.name) {
                    LoginScreen(
                        viewModel = userLoginViewModel,
                        navController = navController,
                        onSignUpClick = {
                            navController.navigate(TaylorSwitchScreen.SignUpPage.name)
                        }
//                        onForgotPasswordClick= {},  // Function to handle "Forgot Password" click
//                        onSignUpClick = {}         // Function to handle "Sign Up" navigation
                    )
                }
                composable(TaylorSwitchScreen.SignUpPage.name) {
                    SignUpScreen(
                        viewModel = userViewModel,
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



@Composable
fun MainUI() {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf(
        MiniFabItems(Icons.Filled.SyncAlt, "Trade"),
        MiniFabItems(Icons.Filled.Gavel, "Bid")
    )
    Column(horizontalAlignment = Alignment.End) {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }) + expandVertically(),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }) + shrinkVertically()
        ) {
            LazyColumn(Modifier.padding(bottom = 8.dp)) {
                items(items.size) {
                    ItemUi(icon = items[it].icon, title = items[it].title)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        val transition = updateTransition(targetState = expanded, label = "transition")
        val rotation by transition.animateFloat(label = "rotation") {
            if (it) 315f else 0f
        }

        FloatingActionButton(
            onClick = { expanded = !expanded }
        ) {
            Icon(
                imageVector = Icons.Filled.Add, contentDescription = "",
                modifier = Modifier.rotate(rotation)
            )
        }
    }
}

@Composable
fun ItemUi(icon: ImageVector, title: String) {
    val context = LocalContext.current
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .border(2.dp, Color(0xFFFF9800), RoundedCornerShape(10.dp))
                .padding(6.dp)
        ) {
            Text(text = title)
        }
        Spacer(modifier = Modifier.width(10.dp))
        FloatingActionButton(onClick = {
            Toast.makeText(context, title, Toast.LENGTH_SHORT).show()
        }, modifier = Modifier.size(45.dp)) {
            Icon(imageVector = icon, contentDescription = "")
        }
    }
}