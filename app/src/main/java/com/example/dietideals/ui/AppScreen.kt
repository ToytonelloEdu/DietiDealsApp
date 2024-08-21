package com.example.dietideals.ui

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dietideals.R
import com.example.dietideals.ui.components.AppBottomBar
import com.example.dietideals.ui.components.AppTopBar
import com.example.dietideals.ui.components.AuctionDetailsTopBarIcon
import com.example.dietideals.ui.components.EditIcon
import com.example.dietideals.ui.components.LogoutIconButton
import com.example.dietideals.ui.components.NotifIconButton
import com.example.dietideals.ui.components.SearchIconButton
import com.example.dietideals.ui.components.SettingsIconButton
import com.example.dietideals.ui.components.textForTopBar
import com.example.dietideals.ui.views.AuctionDetailsView
import com.example.dietideals.ui.views.AuctionsView
import com.example.dietideals.ui.views.BidsView
import com.example.dietideals.ui.views.HomeView
import com.example.dietideals.ui.views.LogInView
import com.example.dietideals.ui.views.MyAuctionDetailsView
import com.example.dietideals.ui.views.NewAuctionView
import com.example.dietideals.ui.views.ProfileView
import com.example.dietideals.ui.views.SignUpView

enum class AppView (@StringRes val title: Int) {
    Home(R.string.app_name),
    Auctions(R.string.auctions),
    Bids(R.string.bids),
    Profile(R.string.profile),
    AuctionDetails(R.string.auction_details),
    MyAuctionDetails(R.string.my_auction_details),
    MyBidAuctionDetails(R.string.my_bid_auction_details),
    UserDetails(R.string.user_details),
    NewAuction(R.string.new_auction),
    LogIn(R.string.log_in),
    SignUp(R.string.sign_up)
}

@Composable
fun AppScreen(
    viewModel: AppViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppView.valueOf(
        backStackEntry?.destination?.route ?: AppView.Home.name
    )
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            val auctionState = uiState.currentAuctionState
            val userState = uiState.userState
            AppTopBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                textForTopBar = { textForTopBar(currentScreen, auctionState, userState) }
            ) {
                if (currentScreen in listOf(
                        AppView.AuctionDetails,
                        AppView.MyAuctionDetails,
                        AppView.MyBidAuctionDetails
                    )
                ){AuctionDetailsTopBarIcon(auctionState)}
                if (currentScreen == AppView.Home) {
                    SearchIconButton { /*TODO*/ }
                }
                if (currentScreen in listOf(
                        AppView.Home,
                        AppView.Auctions,
                )) { NotifIconButton { /*TODO*/ } }
                if (currentScreen == AppView.Profile) {
                    //SettingsIconButton { }
                    LogoutIconButton { viewModel.onLogoutClicked() }
                }

            }
        },
        bottomBar = {
            AppBottomBar(
                currentScreen = currentScreen,
                userState = uiState.userState,
                onAuctionClick = {
                    when (uiState.userState) {
                        is UserState.NotLoggedIn -> {}
                        is UserState.Bidder -> {navController.navigate(AppView.Bids.name)}
                        is UserState.Vendor -> {navController.navigate(AppView.Auctions.name)}
                    }
                },
                onHomeClick = { navController.navigate(AppView.Home.name) },
                onUserClick = { navController.navigate(AppView.Profile.name) }
            )
        },
        floatingActionButton = {
            if(uiState.userState !is UserState.NotLoggedIn) {
                when (currentScreen) {
                    AppView.Profile -> {
                        FloatingActionButton(
                            onClick = { /*TODO*/ },
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            EditIcon(primaryColor = Color.White)
                        }
                    }
                    AppView.Auctions -> {
                        FloatingActionButton(
                            onClick = { navController.navigate(AppView.NewAuction.name) },
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    ) {

        NavHost(
            navController = navController,
            startDestination = AppView.Home.name,
            modifier = Modifier.padding(it),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable(
                AppView.Home.name,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None }
            ) {
                if (uiState.userState is UserState.Vendor) navController.navigate(AppView.Auctions.name)
                else {
                    HomeView(
                        uiState.currentHomeState,
                        onAuctionClicked = { auction, bid ->
                            if(bid && uiState.userState !is UserState.Bidder) navController.navigate(AppView.LogIn.name)
                            else {
                                viewModel.onAuctionClicked(auction, bid)
                                navController.navigate(AppView.AuctionDetails.name)
                            }

                        },
                        onRetry = { viewModel.retryHomePageLoading() },
                        onRefresh = { viewModel.refreshHomePage() }
                    )
                }
            }
            composable(
                AppView.AuctionDetails.name,
                enterTransition = {return@composable slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700))},
                popExitTransition = {return@composable slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700))}
            ) {
                AuctionDetailsView(
                    currentState = uiState.currentAuctionState,
                    onSubmit = { auction, amount ->
                        if(uiState.userState !is UserState.Bidder)
                            navController.navigate(AppView.LogIn.name)
                        else {
                            viewModel.onBidSubmit(auction, amount)
                            navController.navigate(AppView.Bids.name)
                        }
                    }
                )
            }
            composable(AppView.Auctions.name) {
                AuctionsView(
                    vendorUserState = uiState.userState as UserState.Vendor,
                    onAuctionClicked = { auction ->
                        viewModel.onAuctionClicked(auction, false)
                        navController.navigate(AppView.MyAuctionDetails.name)
                    }
                )
            }
            composable(
                AppView.MyAuctionDetails.name,
                enterTransition = {return@composable slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700))},
                popExitTransition = {return@composable slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700))}
            ) {
                MyAuctionDetailsView(
                    currentState = uiState.currentAuctionState,
                    onAccept = {}
                )
            }
            composable(AppView.Bids.name) {
                BidsView(
                    uiState.userState as UserState.Bidder,
                    { auction, bid ->
                        viewModel.onAuctionClicked(auction, bid)
                        navController.navigate(AppView.MyBidAuctionDetails.name)
                    }
                )
            }
            composable(
                AppView.MyBidAuctionDetails.name,
                enterTransition = {return@composable slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700))},
                popExitTransition = {return@composable slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700))}
            ) {
                AuctionDetailsView(
                    currentState = uiState.currentAuctionState,
                    onSubmit = { _, _ ->}
                )
            }
            composable(AppView.Profile.name,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None }
            ) {
                when (uiState.userState) {
                    is UserState.NotLoggedIn -> {
                        navController.navigate(AppView.LogIn.name)
                    }
                    else -> ProfileView(uiState.userState)
                }

            }
            composable(AppView.LogIn.name) {
                val userState = uiState.userState
                if(userState is UserState.NotLoggedIn)
                    LogInView(
                        loginState = userState,
                        onLoginClick = { handle, password ->
                            viewModel.onLoginClicked(
                                handle,
                                password,
                            )
                        },
                        onSignupClick = { navController.navigate(AppView.SignUp.name) }
                    )
                else
                    navController.navigate(AppView.Profile.name)
            }
            composable(AppView.SignUp.name) {
                if (uiState.userState !is UserState.NotLoggedIn){ navController.navigate(AppView.Profile.name) }
                else {
                    val newUser = uiState.signUpState.newUser;
                    val formInvalid =
                        (uiState.signUpState as? SignUpState.Initial)?.formInvalid ?: false
                    SignUpView(
                        newUser = newUser,
                        onValueChange = { passedUser -> viewModel.onSignUpFormChanged(passedUser) },
                        formInvalid = formInvalid,
                        onCancelClick = { navController.navigate(AppView.LogIn.name) },
                        onSignupClick = {
                            viewModel.onSignUpClicked(newUser)
                        }
                    )
                }
            }
            composable(AppView.UserDetails.name) {

            }
            composable(AppView.NewAuction.name) {
                val context = LocalContext.current
                val newAuction = uiState.newAuctionState.newAuction
                NewAuctionView(
                    newAuction = newAuction,
                    onValueChange = { passedAuction ->
                        viewModel.onNewAuctionFormChanged(
                            passedAuction
                        )
                    },
                    newAuctionState = uiState.newAuctionState,
                    exitView = { navController.navigate(AppView.Auctions.name) },
                    onConfirmClick = { auction ->
                        viewModel.onNewAuctionConfirm(auction, context)
                    }
                )
            }
        }
    }

}


