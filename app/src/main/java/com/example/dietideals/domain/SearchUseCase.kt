package com.example.dietideals.domain

import android.util.Log
import com.example.dietideals.data.AppUiState
import com.example.dietideals.data.repos.AuctionsRepository
import com.example.dietideals.domain.auxiliary.SearchQuery
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.ui.HomeFetchState
import com.example.dietideals.ui.SearchQueryState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class SearchUseCase(private val auctionsRepository: AuctionsRepository) {

    suspend fun searchAuctions(state: MutableStateFlow<AppUiState>, query: SearchQuery) {
        try {
            val queriedAuctions: List<Auction>
            withContext(Dispatchers.IO) { queriedAuctions = auctionsRepository.getAuctionsQueried(query) }

            state.update {
                it.copy(
                    currentHomeState = HomeFetchState.HomeSuccess(queriedAuctions),
                    searchQueryState = SearchQueryState.Success(query),
                    isOnline = true,
                    showSearchDialog = false,
                    showAllAuctions = true
                )
            }
        } catch (e: Exception) {
            Log.e("SearchUseCase", "Error: ${e.message}")
            state.update {
                val auctions: List<Auction> = it.currentHomeState.getAuctionsOrNull() ?: emptyList()
                it.copy(
                    currentHomeState = HomeFetchState.Error(auctions, message = e.message),
                    isOnline = (e is HttpException && e.code() == 404)
                )
            }
        }


    }

}