package com.example.dietideals.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dietideals.DietiDealsApplication
import com.example.dietideals.data.AppUiState
import com.example.dietideals.data.repos.BidsRepository
import com.example.dietideals.domain.AuctioneerUseCase
import com.example.dietideals.domain.AuthenticationUseCase
import com.example.dietideals.domain.BuyerUseCase
import com.example.dietideals.domain.HomePageUseCase
import com.example.dietideals.domain.ImageUploadUseCase
import com.example.dietideals.domain.auxiliary.NewAuction
import com.example.dietideals.domain.auxiliary.NewUser
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.domain.models.Auctioneer
import com.example.dietideals.domain.models.Bid
import com.example.dietideals.domain.models.Buyer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.sql.Timestamp


class AppViewModel(
    private val homePageUseCase: HomePageUseCase,
    private val authenticationUseCase: AuthenticationUseCase,
    private val auctioneerUseCase: AuctioneerUseCase,
    private val buyerUseCase: BuyerUseCase,
    private val imageUploadUseCase: ImageUploadUseCase,
    private val bidsRepository: BidsRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private val auctions: List<Auction>
        get() {
            return (_uiState.value.currentHomeState.getAuctionsOrNull()) ?: emptyList()
        }

    init {
        runBlocking {
            authenticationUseCase.rememberLogIn(_uiState)
            viewModelScope.launch {
                authenticationUseCase.checkRemember(_uiState)
                refreshAuctionsOrBids()
            }
        }
        serverHomepageAuctions()
    }

    private fun refreshAuctionsOrBids() {
        when (AuthenticationUseCase.user) {
            is Auctioneer -> refreshUserAuctions()
            is Buyer -> refreshUserBids()
        }
    }

    private fun serverHomepageAuctions() {
        viewModelScope.launch {
                if(homePageUseCase.getHomePageAuctions(_uiState)){
                    launch {
                        homePageUseCase.saveAuctionToDb(auctions)
                    }
                }
        }
    }

    fun onAuctionClicked(passedAuction: Auction, isDirectBid: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(currentAuctionState = AuctionFetchState.AuctionSuccess(passedAuction))
        }
        viewModelScope.launch {
            homePageUseCase.getAuctionDetails(_uiState, passedAuction)
        }
    }

    fun onLoginClicked(handle: String, password: String) {
        viewModelScope.launch {
            authenticationUseCase.logIn(_uiState, handle, password)
        }
    }

    fun onSignUpFormChanged(newUser: NewUser) {
        _uiState.update { currentState ->
            currentState.copy(
                signUpState = SignUpState.Initial(newUser)
            )
        }
    }

    fun onNewAuctionFormChanged(passedAuction: NewAuction) {
        _uiState.update { currentState ->
            currentState.copy(
                newAuctionState = NewAuctionState.Initial(passedAuction)
            )
        }
    }

    fun refreshHomePage() {
        viewModelScope.launch {
            _uiState.update { it.copy(currentHomeState = it.currentHomeState.toggleRefreshing()) }
            if(homePageUseCase.refreshAuction(_uiState)) {
                homePageUseCase.saveAuctionToDb(auctions)
            }

        }
    }

    fun onNewAuctionConfirm(newAuction: NewAuction, context: Context) {
        viewModelScope.launch {
            auctioneerUseCase.createNewAuction(_uiState, newAuction) { uris, auction ->
                uris.forEachIndexed { index, path ->
                    imageUploadUseCase.uploadAuctionImage(context, auction.id!!, index, path)
                }
            }
        }
    }

    fun refreshUserAuctions(swiped: Boolean = false) {
        viewModelScope.launch(
        ) {
            if(swiped) _uiState.update { it.copy(isRefreshing = true) }

            auctioneerUseCase.refreshAuctions(_uiState)
        }
    }

    fun onSignUpClicked(newUser: NewUser) {
        viewModelScope.launch {
            authenticationUseCase.signUp(_uiState, newUser)
        }
    }

    fun onBidSubmit(auction: Auction, amount: Double) {
        viewModelScope.launch {
            try {
                val buyer = (uiState.value.userState as UserState.Bidder).buyer
                val bid = Bid(
                    auction = auction,
                    buyer = buyer,
                    bidder = buyer.username,
                    amount = amount,
                    time = Timestamp(System.currentTimeMillis())
                )
                bidsRepository.postBid(bid, AuthenticationUseCase.token)
                refreshUserBids()
            } catch (e: Exception) {
                Log.e("AppViewModel", "Error: $e -> ${e.printStackTrace()}")
            }
        }
    }

    fun refreshUserBids(swiped: Boolean = false) {
        viewModelScope.launch {
            if(swiped) _uiState.update { it.copy(isRefreshing = true) }
            try {
                val buyer = (uiState.value.userState as UserState.Bidder).buyer
                val bids = bidsRepository.getBidsByUser(buyer.username)
                _uiState.update { currentState ->
                    currentState.copy(
                        userState = UserState.Bidder(buyer.copy(bids = bids.toMutableList())),
                        isRefreshing = false
                    )
                }
            } catch (e: Exception) {
                Log.e("AppViewModel", "Error: ${e.message}")
                _uiState.update {
                    it.copy(isRefreshing = false)
                }
            }
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            authenticationUseCase.logOut(_uiState)
        }
    }

    fun onNewAuctionClicked() {
        _uiState.update { currentState ->
            currentState.copy(
                newAuctionState = NewAuctionState.Initial(
                    NewAuction(auctioneer = AuthenticationUseCase.user as Auctioneer)
                )
            )
        }
    }


    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as DietiDealsApplication)
                val auctionsRepository = application.container.auctionsRepository
                val offlineAuctionsRepository = application.offlineContainer.auctionsRepository
                val usersRepository = application.container.usersRepository
                val offlineUsersRepository = application.offlineContainer.usersRepository
                val authRepository = application.container.authRepository
                val tagsRepository = application.container.tagsRepository
                val imagesRepository = application.container.imagesRepository
                val bidRepository = application.container.bidsRepository
                AppViewModel(
                    homePageUseCase = HomePageUseCase(auctionsRepository, offlineAuctionsRepository),
                    authenticationUseCase = AuthenticationUseCase(authRepository, usersRepository, offlineUsersRepository),
                    auctioneerUseCase = AuctioneerUseCase(auctionsRepository),
                    buyerUseCase = BuyerUseCase(),
                    imageUploadUseCase = ImageUploadUseCase(imagesRepository),
                    bidsRepository = bidRepository
                )
            }
        }
    }

}
