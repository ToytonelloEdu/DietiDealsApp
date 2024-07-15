package com.example.dietideals.data

import com.example.dietideals.ui.AppView
import com.example.dietideals.ui.FetchState

data class AppUiState(
    val userState: UserState = UserState.NotLoggedIn,
    val currentView: AppView = AppView.Home,
    val currentFetchState: FetchState = FetchState.Loading
)
