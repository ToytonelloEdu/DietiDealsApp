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
import com.example.dietideals.data.repos.AuctionsRepository
import com.example.dietideals.data.repos.AuthRepository
import com.example.dietideals.data.repos.BidsRepository
import com.example.dietideals.data.repos.TagsRepository
import com.example.dietideals.data.repos.UsersRepository
import com.example.dietideals.domain.AuthenticationUseCase
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
import retrofit2.HttpException
import java.sql.Timestamp
import java.util.Date


class AppViewModel(
    private val homePageUseCase: HomePageUseCase,
    private val authenticationUseCase: AuthenticationUseCase,
    private val imageUploadUseCase: ImageUploadUseCase,
    private val auctionsRepository: AuctionsRepository,
    private val offlineAuctionsRepository: AuctionsRepository,
    private val usersRepository: UsersRepository,
    private val authRepository: AuthRepository,
    private val tagsRepository: TagsRepository,
    private val bidsRepository: BidsRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private lateinit var _auctions: List<Auction>
    private val auctions: List<Auction>
        get() {
            return (_uiState.value.currentHomeState as? HomeFetchState.HomeSuccess)?.auctions ?: emptyList()
        }

    init {
        //onLoginClicked("ciroanastasio", "forzanapoli")
        serverHomepageAuctions()
    }

    private fun serverHomepageAuctions() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                try {
                    _auctions = auctionsRepository.getAuctions()
                    Log.i("AppViewModel", "Success")
                    currentState.copy(
                        currentHomeState = HomeFetchState.HomeSuccess(_auctions)
                    )
                }
                catch (e: Exception) {
                    _auctions = emptyList()
                    Log.e("AppViewModel", "Error:$e-> ${e.message}")
                    currentState.copy(
                        currentHomeState = HomeFetchState.Error("Error: ${e.message}")
                    )
                }
            }
        }
    }

    fun onAuctionClicked(passedAuction: Auction, isDirectBid: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                currentAuctionState = AuctionFetchState.AuctionSuccess(passedAuction),
            )
        }
        viewModelScope.launch {
            _uiState.update { currentState ->
                try {
                    val auction = auctionsRepository.getAuctionById(passedAuction.id!!)
                    currentState.copy(
                        currentAuctionState = AuctionFetchState.AuctionSuccess(auction, auction.bids)
                    )
                } catch (e: Exception) {
                    Log.e("AppViewModel", "Error: ${e.message}")
                    currentState.copy(
                        currentAuctionState = AuctionFetchState.Error("Error: ${e.message}")
                    )
                }
            }
        }
    }

    fun onLoginClicked(handle: String, password: String) {
        viewModelScope.launch {
            try {
                authRepository.auth(handle, password).let {
                        updateUserStateByHandle(handle)
                        Log.i("AppViewModel", "Response: $it")
                }
            } catch (e: HttpException) {
                Log.e("AppViewModel", "Error: ${e.message}")
                if(e.code() == 401) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            userState = UserState.NotLoggedIn(true)
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("AppViewModel", "Error: ${e.message}")
            }
        }
    }

    private suspend fun updateUserStateByHandle(handle: String) {
        val user = usersRepository.getUserByHandle(handle).let { netUser ->
            when (netUser.userType) {
                "Auctioneer" -> Auctioneer(netUser)
                "Buyer" -> Buyer(netUser)
                else -> throw IllegalArgumentException("NetUser type not found")
            }
        }
        when (user) {
            is Auctioneer -> {
                _uiState.update { currentState ->
                    val newAuction = currentState.newAuctionState.newAuction.apply{
                        auctioneer = user
                    }
                    currentState.copy(
                        userState = UserState.Vendor(user),
                        newAuctionState = NewAuctionState.Initial(newAuction)
                    )
                }
            }
            is Buyer -> {
                _uiState.update { currentState ->
                    currentState.copy(userState = UserState.Bidder(user))
                }
            }
            else -> throw IllegalArgumentException("NetUser type not found")
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

    fun retryHomePageLoading() {
        _uiState.update { currentState ->
            currentState.copy(
                currentHomeState = HomeFetchState.Loading
            )
        }
        serverHomepageAuctions()
    }

    fun refreshHomePage() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    currentHomeState = HomeFetchState.HomeSuccess(auctions, true)
                )
            }
            try{
                _auctions = auctionsRepository.getAuctions()
                _uiState.update { currentState ->
                    currentState.copy(
                        currentHomeState = HomeFetchState.HomeSuccess(_auctions)
                    )
                }
            } catch (e: Exception) {
                Log.e("AppViewModel", "Error: ${e.message}")
            }

        }
    }

    fun onNewAuctionConfirm(newAuction: NewAuction, context: Context) {
        viewModelScope.launch {
            newAuction.date = Date();
            _uiState.update { currentState ->
                currentState.copy(
                    newAuctionState = NewAuctionState.Loading(newAuction)
                )
            }
            try {

                val auction = auctionsRepository.addAuction(newAuction.toAuction(), "")

                newAuction.picturePaths.forEachIndexed { index, path ->
                    imageUploadUseCase.uploadAuctionImage(context, auction.id!!, index, path)
                }

                _uiState.update { currentState ->
                    currentState.copy(
                        newAuctionState = NewAuctionState.Success(newAuction)
                    )
                }

                refreshUserAuctions()
            } catch (e: NullPointerException) {
                Log.e("AppViewModel", "Error: $e -> ${e.message}")
                _uiState.update { currentState ->
                    currentState.copy(
                        newAuctionState = NewAuctionState.Error(newAuction,"Beware, some fields were left empty!")
                    )
                }
            } catch (e: Exception) {
                Log.e("AppViewModel", "Error: ${e.message}")
                _uiState.update { currentState ->
                    currentState.copy(
                        newAuctionState = NewAuctionState.Error(newAuction,"Error: $e -> ${e.message}")
                    )
                }
            }
        }
    }

    private fun refreshUserAuctions() {
        viewModelScope.launch {
            val auctioneer = (uiState.value.userState as UserState.Vendor).auctioneer
            _auctions = auctionsRepository.getAuctionsByUser(auctioneer.username)
            _uiState.update { currentState ->
                currentState.copy(
                    userState = UserState.Vendor(auctioneer.copy(auctions = _auctions.toMutableList())),
                )
            }
        }
    }

    fun onSignUpClicked(newUser: NewUser) {
        viewModelScope.launch {
            authenticationUseCase.signUp(newUser)
        }
    }

    fun onBidSubmit(auction: Auction, amount: Double) {
        viewModelScope.launch {
            try {
                val buyer = (uiState.value.userState as UserState.Bidder).buyer
                val bid = Bid(
                    auction,
                    buyer,
                    buyer.username,
                    amount,
                    Timestamp(System.currentTimeMillis())
                )
                bidsRepository.postBid(bid, "")
                refreshUserBids()
            } catch (e: Exception) {
                Log.e("AppViewModel", "Error: $e -> ${e.printStackTrace()}")
            }
        }
    }

    private fun refreshUserBids() {
        viewModelScope.launch {
            val buyer = (uiState.value.userState as UserState.Bidder).buyer
            val bids = bidsRepository.getBidsByUser(buyer.username)
            _uiState.update { currentState ->
                currentState.copy(
                    userState = UserState.Bidder(buyer.copy(bids = bids.toMutableList())),
                )
            }
        }
    }

    fun onLogoutClicked() {
        _uiState.update { currentState ->
            currentState.copy(
                userState = UserState.NotLoggedIn()
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
                val authRepository = application.container.authRepository
                val tagsRepository = application.container.tagsRepository
                val imagesRepository = application.container.imagesRepository
                val bidRepository = application.container.bidsRepository
                AppViewModel(
                    homePageUseCase = HomePageUseCase(auctionsRepository, offlineAuctionsRepository),
                    authenticationUseCase = AuthenticationUseCase(authRepository, usersRepository),
                    imageUploadUseCase = ImageUploadUseCase(imagesRepository),
                    auctionsRepository = auctionsRepository,
                    offlineAuctionsRepository = offlineAuctionsRepository,
                    usersRepository = usersRepository,
                    authRepository = authRepository,
                    tagsRepository = tagsRepository,
                    bidsRepository = bidRepository
                )
            }
        }
    }

}
