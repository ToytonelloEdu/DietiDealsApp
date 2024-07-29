package com.example.dietideals.data

import com.example.dietideals.domain.auxiliary.NewUser
import com.example.dietideals.ui.AuctionFetchState
import com.example.dietideals.ui.HomeFetchState
import com.example.dietideals.ui.SignUpState
import com.example.dietideals.ui.UserState

data class AppUiState(
    val userState: UserState = UserState.NotLoggedIn(),
    val currentHomeState: HomeFetchState = HomeFetchState.Loading,
    val currentAuctionState: AuctionFetchState = AuctionFetchState.Loading,
    val signUpState: SignUpState = SignUpState.Initial(NewUser())
)