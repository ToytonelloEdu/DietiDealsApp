package com.example.dietideals.domain

import android.net.Uri
import android.util.Log
import com.example.dietideals.data.AppUiState
import com.example.dietideals.data.repos.AuctionsRepository
import com.example.dietideals.data.repos.BidsRepository
import com.example.dietideals.data.repos.UsersRepository
import com.example.dietideals.domain.auxiliary.NewAuction
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.domain.models.Auctioneer
import com.example.dietideals.ui.NewAuctionState
import com.example.dietideals.ui.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.util.Date

class AuctioneerUseCase(
    private val auctionsRepository: AuctionsRepository
) {
    suspend fun createNewAuction(state: MutableStateFlow<AppUiState>, newAuction: NewAuction, images: suspend (MutableList<Uri>, Auction) -> Unit) {
        newAuction.date = Date()
        newAuction.auctioneer = AuthenticationUseCase.user as Auctioneer
        state.update { it.copy(newAuctionState = NewAuctionState.Loading(newAuction)) }

        try {

            val auction = auctionsRepository.addAuction(newAuction.toAuction(), AuthenticationUseCase.token)

            images(newAuction.picturePaths, auction)

            state.update { currentState ->
                currentState.copy(
                    newAuctionState = NewAuctionState.Success(newAuction)
                )
            }

            refreshAuctions(state)
        } catch (e: NullPointerException) {
            Log.e("AppViewModel", "Error: $e -> ${e.message}")
            state.update { currentState ->
                currentState.copy(
                    newAuctionState = NewAuctionState.Error(newAuction,"Beware, some fields were left empty!")
                )
            }
        } catch (e: Exception) {
            Log.e("AppViewModel", "Error: ${e.message}")
            state.update { currentState ->
                currentState.copy(
                    newAuctionState = NewAuctionState.Error(newAuction,"Error: $e -> ${e.message}")
                )
            }
        }
    }


    suspend fun refreshAuctions(state: MutableStateFlow<AppUiState>, ) {
        try {
            var auctions: List<Auction>
            val auctioneer = (state.value.userState as UserState.Vendor).auctioneer
            withContext(Dispatchers.IO) {
                auctions = auctionsRepository.getAuctionsByUser(auctioneer.username)
            }
            state.update { currentState ->
                currentState.copy(
                    userState = UserState.Vendor(auctioneer.copy(auctions = auctions.toMutableList())),
                    isRefreshing = false
                )
            }
        } catch (e: Exception) {
            Log.e("AppViewModel", "Error: ${e.message}")
            state.update { it.copy(isRefreshing = false) }
        }
    }
}

