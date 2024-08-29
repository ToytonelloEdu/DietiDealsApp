package com.example.dietideals.domain

import android.util.Log
import com.example.dietideals.data.AppUiState
import com.example.dietideals.data.repos.AuctionsRepository
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.ui.AuctionFetchState
import com.example.dietideals.ui.HomeFetchState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class HomePageUseCase (
    private val onlineAuctionsRepository: AuctionsRepository,
    private val offlineAuctionsRepository: AuctionsRepository
) {
    //val auctions: MutableList<Auction> = mutableListOf()

    suspend fun getHomePageAuctions(state: MutableStateFlow<AppUiState>): Boolean {
        try {
            val auctions = onlineAuctionsRepository.getAuctions()
            state.update { currentState ->
                Log.i("AppViewModel", "Success")
                currentState.copy(
                    currentHomeState = HomeFetchState.HomeSuccess(auctions),
                    isOnline = true
                )
            }
            return true
        } catch (e: Exception) {
            val auctions = offlineAuctionsRepository.getAuctions()
            Log.e("AppViewModel", "Error:$e-> ${e.message}")
            state.update { currentState ->
                currentState.copy(
                    currentHomeState = HomeFetchState.Error(auctions, message = e.message),
                    isOnline = false
                )
            }
            return false
        }
    }

    suspend fun saveAuctionToDb(auctions: List<Auction>) {
        auctions.forEach {
            if(!it.hasBeenOverFor(3))
                offlineAuctionsRepository.addAuction(it)
        }
    }

    suspend fun refreshAuction(state: MutableStateFlow<AppUiState>) : Boolean {
        try{
            val auctions = onlineAuctionsRepository.getAuctions()
            state.update { currentState ->
                currentState.copy(
                    currentHomeState = HomeFetchState.HomeSuccess(auctions),
                    isOnline = true
                )
            }
            return true
        } catch (e: Exception) {
            Log.e("AppViewModel", "Error: ${e.message}")
            val auctions = state.value.currentHomeState.getAuctionsOrNull() ?: emptyList()
            state.update { currentState ->
                currentState.copy(
                    currentHomeState = HomeFetchState.Error(auctions, message = e.message),
                    isOnline = false
                )
            }
            return false
        }
    }

    suspend fun getAuctionDetails(state: MutableStateFlow<AppUiState>, passedAuction: Auction) {
        state.update { currentState ->
            try {
                val auction = onlineAuctionsRepository.getAuctionById(passedAuction.id!!)
                currentState.copy(
                    currentAuctionState = AuctionFetchState.AuctionSuccess(auction)
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