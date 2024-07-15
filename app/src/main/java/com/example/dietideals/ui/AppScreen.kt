package com.example.dietideals.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dietideals.R
import com.example.dietideals.ui.components.AppBottomBar
import com.example.dietideals.ui.components.AppTopBar
import com.example.dietideals.ui.views.AucitonView
import com.example.dietideals.ui.views.BidsView
import com.example.dietideals.ui.views.HomeView
import com.example.dietideals.ui.views.ProfileView

enum class AppView (@StringRes val title: Int) {
    Home(R.string.app_name),
    Auctions(R.string.auctions),
    Bids(R.string.bids),
    Profile(R.string.profile)
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

    Scaffold(
        topBar = { AppTopBar(currentScreen) },
        bottomBar = { AppBottomBar(currentScreen) }
    ) {
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = AppView.Home.name,
            modifier = Modifier.padding(it)
        ) {
            composable(AppView.Home.name) {
                HomeView(
                    uiState.currentFetchState
                )
            }
            composable(AppView.Auctions.name) {
                AucitonView(

                )
            }
            composable(AppView.Bids.name) {
                BidsView(

                )
            }
            composable(AppView.Profile.name) {
                ProfileView(

                )
            }
        }
    }

}
