package com.example.dietideals.ui

import com.example.dietideals.domain.ausiliary.NewUser
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.domain.models.Auctioneer
import com.example.dietideals.domain.models.Bid
import com.example.dietideals.domain.models.Buyer


sealed interface UserState {
    data class NotLoggedIn(val wrongCredentials: Boolean = false) : UserState
    data class Bidder(val buyer: Buyer) : UserState
    data class Vendor(val auctioneer: Auctioneer) : UserState
}

sealed interface HomeFetchState {
    data class HomeSuccess(val auctions: List<Auction>) : HomeFetchState
    data object Loading : HomeFetchState
    data class Error(val message: String? = null) : HomeFetchState
}

sealed interface AuctionFetchState {
    data class AuctionSuccess(val auction: Auction, val bids: List<Bid>? = null, val photos: List<String> = emptyList()) : AuctionFetchState
    data object Loading : AuctionFetchState
    data class Error(val message: String? = null) : AuctionFetchState
}

sealed class SignUpState (val newUser: NewUser) {
    class Initial(newUser: NewUser, val formInvalid: Boolean = false) : SignUpState(newUser)
    class Loading(newUser: NewUser) : SignUpState(newUser)
    class Error(newUser: NewUser, val message: String? = null) : SignUpState(newUser)
}
