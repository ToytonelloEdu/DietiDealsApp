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
import com.example.dietideals.domain.AuctioneerUseCase
import com.example.dietideals.domain.AuthenticationUseCase
import com.example.dietideals.domain.BuyerUseCase
import com.example.dietideals.domain.HomePageUseCase
import com.example.dietideals.domain.ImageUploadUseCase
import com.example.dietideals.domain.NotificationsUseCase
import com.example.dietideals.domain.SearchUseCase
import com.example.dietideals.domain.auxiliary.FormField
import com.example.dietideals.domain.auxiliary.NewAuction
import com.example.dietideals.domain.auxiliary.NewUser
import com.example.dietideals.domain.auxiliary.ProfileForm
import com.example.dietideals.domain.auxiliary.SearchQuery
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.domain.models.Auctioneer
import com.example.dietideals.domain.models.Bid
import com.example.dietideals.domain.models.Buyer
import com.example.dietideals.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class AppViewModel(
    private val homePageUseCase: HomePageUseCase,
    private val authenticationUseCase: AuthenticationUseCase,
    private val auctioneerUseCase: AuctioneerUseCase,
    private val buyerUseCase: BuyerUseCase,
    private val imageUploadUseCase: ImageUploadUseCase,
    private val searchUseCase: SearchUseCase,
    private val notificationsUseCase: NotificationsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private val auctions: List<Auction>
        get() = (_uiState.value.currentHomeState.getAuctionsOrNull()) ?: emptyList()

    val navigationEnabled: Boolean
        get() = !_uiState.value.showSearchDialog && !_uiState.value.showNotificationsDialog

    init {
        runBlocking {
            authenticationUseCase.rememberLogIn(_uiState)
            viewModelScope.launch {
                authenticationUseCase.checkToken(_uiState)
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
            currentState.copy(
                currentAuctionState = AuctionFetchState.AuctionSuccess(passedAuction),
                isDirectBid = isDirectBid
            )
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
            if(AuthenticationUseCase.token == null) authenticationUseCase.checkToken(_uiState)


            auctioneerUseCase.createNewAuction(_uiState, newAuction) { uris, auction ->
                uris.forEachIndexed { index, path ->
                    imageUploadUseCase.uploadAuctionImage(context, auction.id!!, index, path)
                }
            }
        }
    }

    fun refreshUserAuctions(swiped: Boolean = false) {
        viewModelScope.launch {
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
            if(AuthenticationUseCase.token == null) authenticationUseCase.checkToken(_uiState)

            //if
            buyerUseCase.createNewBid(_uiState, auction, amount)
                //homePageUseCase.updateSingleAuction(_uiState, auction)
        }
    }

    fun refreshUserBids(swiped: Boolean = false) {
        viewModelScope.launch {
            if(swiped) _uiState.update { it.copy(isRefreshing = true) }

            buyerUseCase.refreshBids(_uiState)
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            authenticationUseCase.logOut(_uiState)
        }
    }

    fun onNewAuctionClicked() {
        _uiState.update { currentState ->
            val auctioneer = AuthenticationUseCase.user as Auctioneer
            currentState.copy(
                newAuctionState = NewAuctionState.Initial(
                    NewAuction(auctioneer = FormField(auctioneer) { it != null })
                )
            )
        }
    }

    fun showSearchDialog(show: Boolean = !_uiState.value.showSearchDialog) {
        _uiState.update {
            it.copy(
                showSearchDialog = show,
                showNotificationsDialog = false
            )
        }
    }

    fun showNotificationsDialog(show: Boolean = !_uiState.value.showNotificationsDialog) {
        viewModelScope.launch {
            if (show) launch { notificationsUseCase.refreshNotifications(_uiState) }

            _uiState.update {
                it.copy(
                    showNotificationsDialog = show,
                    showSearchDialog = false
                )
            }
        }
    }

    fun searchQueryChange(searchQuery: SearchQuery) {
        _uiState.update {
            it.copy(searchQueryState = SearchQueryState.Initial(searchQuery))
        }
    }

    fun onSearchSubmit(query: SearchQuery) {
        viewModelScope.launch {
            searchUseCase.searchAuctions(_uiState, query)
        }
    }

    fun onProfileEditStart() {
        val user = AuthenticationUseCase.user!!
        _uiState.update { currentState ->
            val profileForm = currentState.profileEditState.profileForm
            copyProfileInfos(profileForm, user)
            currentState.copy(
                isEditingProfile = true,
            )
        }
    }

    fun onProfileEditConfirm(profileForm: ProfileForm, context: Context) {
        viewModelScope.launch {
            val success = authenticationUseCase.editUserProfile(_uiState, profileForm) {uri, username ->
                imageUploadUseCase.uploadProfileImage(context, username, uri)
            }
            if(success) onProfileEditCancel()
        }

    }

    fun onProfileEditCancel() {
        _uiState.update { currentState ->
            currentState.copy(
                isEditingProfile = false,
                profileEditState = ProfileEditState.Initial(ProfileForm())
            )
        }
    }

    fun onProfileFormChanged(it: ProfileForm) {
        _uiState.update { currentState ->
            currentState.copy(profileEditState = ProfileEditState.Initial(it))
        }
    }

    private fun copyProfileInfos(
        profileForm: ProfileForm,
        user: User,
    ) {
        profileForm.bio.value = user.bio
        profileForm.nationality.value = user.nationality
        profileForm.website.value = user.links?.website
        profileForm.instagram.value = user.links?.instagram
        profileForm.facebook.value = user.links?.facebook
        profileForm.twitter.value = user.links?.twitter
    }

    fun onAuctioneerClicked(username: String) {
        viewModelScope.launch {
            authenticationUseCase.getUser(_uiState, username)
        }
    }

    fun onAuctionAccept(auction: Auction, bid: Bid) {
        viewModelScope.launch {
            auctioneerUseCase.acceptBid(_uiState, auction, bid)
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
                val bidsRepository = application.container.bidsRepository
                val notificationsRepository = application.container.notificationsRepository
                val offlineNotificationsRepository = application.offlineContainer.notificationsRepository

                AppViewModel(
                    homePageUseCase = HomePageUseCase(auctionsRepository, offlineAuctionsRepository),
                    authenticationUseCase = AuthenticationUseCase(authRepository, usersRepository, offlineUsersRepository),
                    auctioneerUseCase = AuctioneerUseCase(auctionsRepository),
                    buyerUseCase = BuyerUseCase(bidsRepository),
                    imageUploadUseCase = ImageUploadUseCase(imagesRepository),
                    searchUseCase = SearchUseCase(auctionsRepository),
                    notificationsUseCase = NotificationsUseCase(notificationsRepository, offlineNotificationsRepository, offlineUsersRepository)
                )
            }
        }
    }

}
