package com.example.taylorswitch

import android.os.Build
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
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ManageSearch
import androidx.compose.material.icons.outlined.PostAdd
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material3.Button
import androidx.compose.material.icons.outlined.SyncAlt
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
import com.example.taylorswitch.ui.Trade.ViewModel.TradeViewModel
import com.example.taylorswitch.ui.Trade.uiScreen.HomepageScreen
import com.example.taylorswitch.ui.Trade.uiScreen.PostTradeItemScreen
import com.example.taylorswitch.ui.Trade.uiScreen.ReviewTradeRequest
import com.example.taylorswitch.ui.Trade.uiScreen.TradeHistoryScreen
import com.example.taylorswitch.ui.Trade.uiScreen.TradeListScreen
import com.example.taylorswitch.ui.Trade.uiScreen.TradeRequestScreen
import com.example.taylorswitch.ui.user.UserViewmodel.UserLoginViewModel
import com.example.taylorswitch.ui.user.UserViewmodel.UserViewModel
import com.example.taylorswitch.ui.user.login.LoginScreen
import com.example.taylorswitch.ui.user.signup.SignUpScreen
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
    BidMainPage,
    ViewBid,
    PostBid,
    BidRecord,
    BidPost,
    Test,
    PostTrade,
    TradeHomePage,
    RequestTrade,
    ReviewTrade,
    TradeList,
    TradeHistory
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaylorSwitchApp(
    bidViewModel: BidViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    ),
    tradeViewModel: TradeViewModel = viewModel(),
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

    //bottom app bar icon

    val tradeTab = TabBarItem(
        title = "Trade",
        path = TaylorSwitchScreen.PostBid.name,
        selectedIcon = Icons.Filled.SyncAlt,
        unselectedIcon = Icons.Outlined.SyncAlt
    )
    val bidTab = TabBarItem(
        title = "Bid",
        path = TaylorSwitchScreen.BidMainPage.name,
        selectedIcon = Icons.Filled.Gavel,
        unselectedIcon = Icons.Outlined.Gavel
    )

    val bidRTab = TabBarItem(
        title = "Bid",
        path = TaylorSwitchScreen.BidRecord.name,
        selectedIcon = Icons.Filled.Gavel,
        unselectedIcon = Icons.Outlined.Gavel
    )
    val bidPTab = TabBarItem(
        title = "Post",
        path = TaylorSwitchScreen.BidPost.name,
        selectedIcon = Icons.Filled.PostAdd,
        unselectedIcon = Icons.Outlined.PostAdd
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
    val bidBarItems = listOf(bidRTab, bidPTab)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val appUiState by userLoginViewModel.appUiState.collectAsState()
    val tUiState by tradeViewModel.tUiState.collectAsState()

    if (!(currentDestination == TaylorSwitchScreen.LoginPage.name || currentDestination == TaylorSwitchScreen.SignUpPage.name)) {
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
                            Text(text = appUiState.username, fontSize = 15.sp)
                        }
                    }

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
                    Column() {

                        Text("Bid History", modifier = Modifier.padding(16.dp))
                        HorizontalDivider()
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.Home,
                                    contentDescription = "home"
                                )
                            },
                            label = {
                                Text(text = "Home Page")
                            },
                            selected = false,
                            onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                                navController.navigate(TaylorSwitchScreen.BidMainPage.name)

                            }
                        )
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.ManageSearch,
                                    contentDescription = "search"
                                )
                            },
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
                                navController.navigate(TaylorSwitchScreen.BidRecord.name)

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
                                navController.navigate(TaylorSwitchScreen.BidRecord.name)

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
                        )

                        NavigationDrawerItem(
                            label = { Text("logout") },
                            selected = false,
                            onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                                userLoginViewModel.signOut()
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
                            label = { Text("Trade Home Page") },
                            selected = false,
                            onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                                navController.navigate(TaylorSwitchScreen.TradeHomePage.name)
                            }
                        )
                        NavigationDrawerItem(
                                label = { Text("Post Trade") },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                            navController.navigate(TaylorSwitchScreen.PostTrade.name)
                        }
                        )
                        NavigationDrawerItem(
                            label = { Text("Trade List") },
                            selected = false,
                            onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                                navController.navigate(TaylorSwitchScreen.TradeList.name)
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text("Trade History") },
                            selected = false,
                            onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                                navController.navigate(TaylorSwitchScreen.TradeHistory.name)
                            }
                        )
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    if (currentDestination == TaylorSwitchScreen.ViewBid.name + "/{auctionId}") {
                        TaylorSwitchAppBar(
                            title = "Bid",
                            canNavigateBack = true,
                            navigateUp = { navController.popBackStack() },
                            onNavigationIconClick = {}
                        )
                    }else if(currentDestination == TaylorSwitchScreen.BidMainPage.name){
                        TaylorSwitchAppBar(
                            title = "Taylor Switch",
                            canNavigateBack = false,
                            navigateUp = {},
                            onNavigationIconClick = {scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                            }
                        )
                    }else if (currentDestination == TaylorSwitchScreen.EditProfilePage.name) {
                        TaylorSwitchAppBar(
                            title = "Edit Profile",
                            canNavigateBack = true,
                            navigateUp = { navController.popBackStack() },
                            onNavigationIconClick = {}
                        )
                    }else if(currentDestination == TaylorSwitchScreen.BidPost.name){
                        TaylorSwitchAppBar(
                            title = "Bid Post",
                            canNavigateBack = false,
                            navigateUp = {},
                            onNavigationIconClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                    }else if(currentDestination == TaylorSwitchScreen.BidRecord.name){
                        TaylorSwitchAppBar(
                            title = "Bid Record",
                            canNavigateBack = false,
                            navigateUp = {},
                            onNavigationIconClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                    }else if(currentDestination == TaylorSwitchScreen.TradeHomePage.name){
                        TaylorSwitchAppBar(
                            title = "Taylor Switch",
                            canNavigateBack = false,
                            navigateUp = {},
                            onNavigationIconClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                    }else if(currentDestination == TaylorSwitchScreen.PostTrade.name){
                        TaylorSwitchAppBar(
                            title = "Post Trade",
                            canNavigateBack = false,
                            navigateUp = {},
                            onNavigationIconClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                    }else if(currentDestination == TaylorSwitchScreen.RequestTrade.name + "/{auctionId}"){
                        TaylorSwitchAppBar(
                            title = "Trade",
                            canNavigateBack = false,
                            navigateUp = {},
                            onNavigationIconClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                    }else if(currentDestination == TaylorSwitchScreen.ReviewTrade.name + "/{auctionId}"){
                        TaylorSwitchAppBar(
                            title = "Trade",
                            canNavigateBack = false,
                            navigateUp = {},
                            onNavigationIconClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                    }else if(currentDestination == TaylorSwitchScreen.TradeList.name){
                        TaylorSwitchAppBar(
                            title = "Trade List",
                            canNavigateBack = false,
                            navigateUp = {},
                            onNavigationIconClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                    }else if(currentDestination == TaylorSwitchScreen.TradeHistory.name){
                        TaylorSwitchAppBar(
                            title = "Trade Record",
                            canNavigateBack = false,
                            navigateUp = {},
                            onNavigationIconClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                    }
                    else {
                        TaylorSwitchAppBar(
                            title = currentDestination.toString(),
                            canNavigateBack = true,
                            navigateUp = { navController.popBackStack() },
                            onNavigationIconClick = {}
                        )
                    }
                },
                bottomBar = {
                    if (currentDestination == TaylorSwitchScreen.BidMainPage.name) {
                        TaylorSwitchBottomBar(
                            tabBarItems = tabBarItems,
                            navController = navController
                        )
                    }else if(currentDestination == TaylorSwitchScreen.BidPost.name || currentDestination == TaylorSwitchScreen.BidRecord.name){
                        TaylorSwitchBottomBar(
                            tabBarItems = bidBarItems,
                            navController = navController
                        )
                    }
                },
                floatingActionButton = {
                    if (currentDestination == TaylorSwitchScreen.BidMainPage.name) {
                        MainUI(navController = navController)
                    }
                }
            )
            { innerPadding ->
                val uiState by bidViewModel.uiState.collectAsState()
                NavHost(
                    navController = navController,
                    startDestination = TaylorSwitchScreen.LoginPage.name,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(TaylorSwitchScreen.BidMainPage.name) {
//                        bidViewModel.fetchUserProfile()
                        HomeScreen(
                            viewModel = bidViewModel,
                            navController = navController
                        )
                    }
                    //Bid

                    composable(route = TaylorSwitchScreen.PostBid.name) {
//                    viewModel.resetUiState()
                        PostScreen(
                            onPostButtonClicked = {
                                if(bidViewModel.postBid(poster = appUiState.uid, context = context)){
                                    navController.navigate(TaylorSwitchScreen.BidPost.name)
                                }

                            },
                            bidViewModel = bidViewModel,
                            onCancelButtonClicked = {
                                bidViewModel.resetPosting()
                                navController.navigate(TaylorSwitchScreen.BidMainPage.name)
                            }
                        )
//                    fab()
                    }

                    composable(TaylorSwitchScreen.ViewBid.name + "/{auctionId}") { backStackEntry ->
                        val auctionId = backStackEntry.arguments?.getString("auctionId")
//                        bidViewModel.getAuctionById("$auctionId")
                        BidSession(
                            auctionId = auctionId,
                            bidUiState = uiState,
                            bidViewModel = bidViewModel,
                            navController = navController
                        )
                    }

                    composable(TaylorSwitchScreen.BidPost.name) {

                        bidViewModel.getUserHistoryArray("userPost", "postRef")
                        PostHistoryScreen(
                            bidViewModel = bidViewModel,
                            list = uiState.historyRecArr,
                            navController = navController

                        )
                    }
                    composable(TaylorSwitchScreen.BidRecord.name) {
                        bidViewModel.getUserHistoryArray("userBid", "bidRef")
                        BidHistoryScreen(
                            bidViewModel = bidViewModel,
                            list = uiState.historyRecArr,
                            navController = navController

                        )
                    }
                    composable(TaylorSwitchScreen.Test.name) {
//                    MultiplePhotoPicker()
//                        SignUpScreen(userViewModel, navController)
                    }
                    composable(TaylorSwitchScreen.PostTrade.name){
                        PostTradeItemScreen(tradeViewModel,{})
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
                    composable(TaylorSwitchScreen.EditProfilePage.name) {
                        EditProfileScreen(
                            viewModel = userProfileViewModel,
                            onBackClick = { navController.navigate(TaylorSwitchScreen.BidMainPage.name) },
                            navController = navController
//                        onForgotPasswordClick= {},  // Function to handle "Forgot Password" click
//                        onSignUpClick = {}         // Function to handle "Sign Up" navigation
                        )
                    }
                    composable(TaylorSwitchScreen.TradeHomePage.name) {
                        HomepageScreen(
                            tradeViewModel = tradeViewModel,
                            navController = navController
                        )
                    }
                    composable(TaylorSwitchScreen.PostTrade.name) {
                        PostTradeItemScreen(
                            tradeViewModel = tradeViewModel,
                            onPostButtonClicked = {
                                tradeViewModel.postTrade(owner = appUiState.uid, context = context)
                                navController.navigate(TaylorSwitchScreen.TradeHomePage.name)
                                                  },
                            onCancelButtonClicked = {
                                tradeViewModel.resetPosting()
                                navController.navigate(TaylorSwitchScreen.TradeHomePage.name)
                            }
                        )
                    }
                    composable(TaylorSwitchScreen.RequestTrade.name + "/{tradeId}") { backStackEntry ->
                        val tradeId = backStackEntry.arguments?.getString("tradeId")
                        tradeViewModel.getTradeById("$tradeId")
                        TradeRequestScreen(
                            tradeId = tradeId,
                            tradeUiState = tUiState,
                            tradeViewModel = tradeViewModel,
                            navController = navController
                        )
                    }
                    composable(TaylorSwitchScreen.ReviewTrade.name + "/{tradeId}") { backStackEntry ->
                        val tradeId = backStackEntry.arguments?.getString("tradeId")
                        tradeViewModel.getTradeById("$tradeId")
                        if (tradeId != null) {
                            ReviewTradeRequest(
                                tradeId = tradeId,
                                tradeUiState = tUiState,
                                tradeViewModel = tradeViewModel,
                                navController = navController
                            )
                        }
                    }
                    composable(TaylorSwitchScreen.TradeList.name) {
                        tradeViewModel.getUserHistoryArray("userPost", "postRef")
                        TradeListScreen(
                            tradeViewModel = tradeViewModel,
                            list = tUiState.tradeHistoryArr,
                            navController = navController
                        )
                    }
                    composable(TaylorSwitchScreen.TradeHistory.name) {
                        tradeViewModel.getUserHistoryArray("userTrade", "tradeRef")
                        TradeHistoryScreen(
                            tradeViewModel = tradeViewModel,
                            list = tUiState.tradeHistoryArr,
                            navController = navController
                        )
                    }
                }
            }
        }
    } else {
        Scaffold(
            topBar = {
                if(currentDestination == TaylorSwitchScreen.LoginPage.name){
                    TaylorSwitchAppBar(
                        title = "Taylor Switch",
                        canNavigateBack = false,
                        navigateUp = { /* TODO: implement back navigation */ },
                        onNavigationIconClick = {},
                        disableSidebar = true
                    )
                }else if(currentDestination == TaylorSwitchScreen.SignUpPage.name){
                    TaylorSwitchAppBar(
                        title = "Sign Up",
                        canNavigateBack = true,
                        navigateUp = { navController.popBackStack() },
                        onNavigationIconClick = {}
                    )
                }

            }
        ) {
            innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = TaylorSwitchScreen.LoginPage.name,
                    modifier = Modifier.padding(innerPadding)
                ) {
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
                    composable(TaylorSwitchScreen.BidMainPage.name) {
//                        bidViewModel.getUserProfile()
                        HomeScreen(
                            viewModel = bidViewModel,
                            navController = navController
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


@Composable
fun MainUI(navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf(
        MiniFabItems(Icons.Filled.SyncAlt, "Trade", TaylorSwitchScreen.PostTrade.name),
        MiniFabItems(Icons.Filled.Gavel, "Bid", TaylorSwitchScreen.PostBid.name)
    )
    Column(horizontalAlignment = Alignment.End) {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }) + expandVertically(),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }) + shrinkVertically()
        ) {
            LazyColumn(Modifier.padding(bottom = 8.dp)) {
                items(items.size) {
                    ItemUi(icon = items[it].icon, title = items[it].title, route = items[it].route, navController = navController)
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
fun ItemUi(icon: ImageVector, title: String, route: String, navController: NavHostController) {
    val context = LocalContext.current
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .border(2.dp, Color.Gray, RoundedCornerShape(10.dp))
                .padding(6.dp)
        ) {
            Text(text = title)
        }
        Spacer(modifier = Modifier.width(10.dp))
        FloatingActionButton(onClick = {
            navController.navigate(route)
        }, modifier = Modifier.size(45.dp)) {
            Icon(imageVector = icon, contentDescription = "")
        }
    }
}