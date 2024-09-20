package com.example.dietideals.domain

import android.util.Log
import com.example.dietideals.data.AppUiState
import com.example.dietideals.data.repos.BidsRepository
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.domain.models.Bid
import com.example.dietideals.domain.models.Buyer
import com.example.dietideals.ui.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.sql.Timestamp

class BuyerUseCase(
    private val bidsRepository: BidsRepository
) {

    suspend fun createNewBid(
        state: MutableStateFlow<AppUiState>,
        auction: Auction,
        amount: Double,
    ) {
        try {
            var bid = instanciateBid(auction, amount)
            withContext(Dispatchers.IO) {
                bid = bidsRepository.postBid(bid, AuthenticationUseCase.token)
            }
            refreshBids(state)
            //return true
        } catch (e: Exception) {
            Log.e("AppViewModel", "Error: $e -> ${e.printStackTrace()}")
            //return false
        }
    }

    suspend fun refreshBids(state: MutableStateFlow<AppUiState>) {
        try {
            val buyer = (state.value.userState as UserState.Bidder).buyer
            val bids = bidsRepository.getBidsByUser(buyer.username)
            state.update { currentState ->
                currentState.copy(
                    userState = UserState.Bidder(buyer.copy(bids = bids.toMutableList())),
                    isRefreshing = false,
                    isOnline = true
                )
            }
        } catch (e: Exception) {
            Log.e("AppViewModel", "Error: ${e.message}")
            state.update { it.copy(isRefreshing = false, isOnline = false) }
        }
    }

    private fun instanciateBid(auction: Auction, amount: Double): Bid {
        val buyer = AuthenticationUseCase.user as Buyer
        return Bid(
            auction = auction,
            amount = amount,
            buyer = buyer,
            bidder = buyer.username,
            time = Timestamp(System.currentTimeMillis())
        )
    }
}