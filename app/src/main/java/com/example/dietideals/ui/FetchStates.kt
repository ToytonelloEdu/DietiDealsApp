package com.example.dietideals.ui

import com.example.dietideals.domain.auxiliary.NewAuction
import com.example.dietideals.domain.auxiliary.NewUser
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.domain.models.Auctioneer
import com.example.dietideals.domain.models.Bid
import com.example.dietideals.domain.models.Buyer
import com.example.dietideals.domain.models.User


sealed interface UserState {
    data class NotLoggedIn(val wrongCredentials: Boolean = false) : UserState
    data class Bidder(val buyer: Buyer) : UserState
    data class Vendor(val auctioneer: Auctioneer) : UserState

    fun getUserOrNull(): User? {
        return when(this){
            is Vendor -> auctioneer
            is Bidder -> buyer
            is NotLoggedIn -> null
        }
    }
}

sealed interface HomeFetchState {
    data class HomeSuccess(val auctions: List<Auction>, val isRefreshing: Boolean = false) : HomeFetchState
    data object Loading : HomeFetchState
    data class Error(val auctions: List<Auction>, val isRefreshing: Boolean = false, val message: String? = null) : HomeFetchState

    fun getAuctionsOrNull(): List<Auction>? {
        return when(this){
            is HomeSuccess -> auctions
            is Error -> auctions
            is Loading -> null
        }
    }

    fun toggleRefreshing(): HomeFetchState {
        return when(this){
            is HomeSuccess -> HomeSuccess(auctions, !isRefreshing)
            is Error -> HomeSuccess(auctions, !isRefreshing)
            is Loading -> this
        }
    }

}

sealed interface AuctionFetchState {
    data class AuctionSuccess(val auction: Auction) : AuctionFetchState
    data object Loading : AuctionFetchState
    data class Error(val message: String? = null) : AuctionFetchState
}

sealed class SignUpState (val newUser: NewUser) {
    class Initial(newUser: NewUser, val formInvalid: Boolean = false) : SignUpState(newUser)
    class Loading(newUser: NewUser) : SignUpState(newUser)
    class Success(newUser: NewUser): SignUpState(newUser)
    class Error(newUser: NewUser, val message: String? = null) : SignUpState(newUser)
}

sealed class NewAuctionState(val newAuction: NewAuction) {
    class Initial(newAuction: NewAuction, val formValid: Boolean = true) : NewAuctionState(newAuction)
    class Loading(newAuction: NewAuction) : NewAuctionState(newAuction)
    class Success(newAuction: NewAuction) : NewAuctionState(newAuction)
    class Error(newAuction: NewAuction, val message: String? = null) : NewAuctionState(newAuction)
}
