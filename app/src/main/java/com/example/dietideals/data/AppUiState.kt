package com.example.dietideals.data

import com.example.dietideals.domain.auxiliary.NewAuction
import com.example.dietideals.domain.auxiliary.NewUser
import com.example.dietideals.domain.auxiliary.SearchQuery
import com.example.dietideals.ui.AuctionFetchState
import com.example.dietideals.ui.HomeFetchState
import com.example.dietideals.ui.NewAuctionState
import com.example.dietideals.ui.SearchQueryState
import com.example.dietideals.ui.SignUpState
import com.example.dietideals.ui.UserState

data class AppUiState(
    val userState: UserState = UserState.NotLoggedIn(),
    val currentHomeState: HomeFetchState = HomeFetchState.Loading,
    val currentAuctionState: AuctionFetchState = AuctionFetchState.Loading,
    val signUpState: SignUpState = SignUpState.Initial(NewUser()),
    val newAuctionState: NewAuctionState = NewAuctionState.Initial(NewAuction()),
    val searchQueryState: SearchQueryState = SearchQueryState.Initial(SearchQuery()),

    val showSearchDialog: Boolean = false,
    val showNotificationsDialog: Boolean = false,
    val showAllAuctions: Boolean = false,
    val isRefreshing: Boolean = false,
    val isDirectBid: Boolean = false,
    val isOnline: Boolean = true
)