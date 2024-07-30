package com.example.dietideals.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dietideals.DietiDealsApplication
import com.example.dietideals.MainActivity
import com.example.dietideals.data.AppUiState
import com.example.dietideals.data.repos.AuctionsRepository
import com.example.dietideals.data.repos.AuthRepository
import com.example.dietideals.data.repos.ImagesRepository
import com.example.dietideals.data.repos.TagsRepository
import com.example.dietideals.data.repos.UsersRepository
import com.example.dietideals.domain.AuthenticationUseCase
import com.example.dietideals.domain.HomePageUseCase
import com.example.dietideals.domain.auxiliary.NewAuction
import com.example.dietideals.domain.auxiliary.NewUser
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.domain.models.Auctioneer
import com.example.dietideals.domain.models.Buyer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File


class AppViewModel(
    private val homePageUseCase: HomePageUseCase,
    private val authenticationUseCase: AuthenticationUseCase,
    private val auctionsRepository: AuctionsRepository,
    private val offlineAuctionsRepository: AuctionsRepository,
    private val usersRepository: UsersRepository,
    private val authRepository: AuthRepository,
    private val tagsRepository: TagsRepository,
    private val imagesRepository: ImagesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private lateinit var auctions: List<Auction>

    init {
        serverHomepageAuctions()
    }

    private fun serverHomepageAuctions() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                try {
                    auctions = auctionsRepository.getAuctions()
                    currentState.copy(
                        currentHomeState = HomeFetchState.HomeSuccess(auctions)
                    )
                }
                catch (e: Exception) {
                    auctions = emptyList()
                    Log.e("AppViewModel", "Error: ${e.message}")
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
                else -> throw IllegalArgumentException("User type not found")
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
            else -> throw IllegalArgumentException("User type not found")
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
                AppViewModel(
                    homePageUseCase = HomePageUseCase(auctionsRepository, offlineAuctionsRepository),
                    authenticationUseCase = AuthenticationUseCase(authRepository, usersRepository),
                    auctionsRepository = auctionsRepository,
                    offlineAuctionsRepository = offlineAuctionsRepository,
                    usersRepository = usersRepository,
                    authRepository = authRepository,
                    tagsRepository = tagsRepository,
                    imagesRepository = imagesRepository
                )
            }
        }
    }

}
