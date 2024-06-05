package com.example.dietideals.data

import com.example.dietideals.ui.AppView

data class AppUiState(
    val userState: UserState = UserState.NotLoggedIn,
    val currentView: AppView = AppView.Home,
    val serverString: String = ""
)
