package com.example.dietideals.ui

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
import com.example.dietideals.data.repos.StringsRepository
import com.example.dietideals.data.repos.TagsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface FetchState {
    data class HomeSuccess(val auctions: String) : FetchState
    data object Loading : FetchState
    data class Error(val message: String? = null) : FetchState
}

class AppViewModel(
    private val stringsRepository: StringsRepository,
    private val auctionsRepository: AuctionsRepository,
    private val tagsRepository: TagsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()


    init {
        serverString()

            viewModelScope.launch {
                try {
                    tagsRepository.getTags().map {
                        Log.i("AppViewModel", "Tags: $it")
                    }
                } catch (e: IllegalArgumentException) {
                    Log.e("AppViewModel", "Error: ${e.message}")
                }
            }


    }

    private fun serverString() {
        var auctions: String
        viewModelScope.launch {
            _uiState.update { currentState ->
                try {
                    auctions = stringsRepository.getString()
                    currentState.copy(
                        currentFetchState = FetchState.HomeSuccess(auctions)
                    )
                }
                catch (e: IOException) {
                    auctions = "Error"
                    Log.e("AppViewModel", "Error: ${e.message}")
                    currentState.copy(
                        currentFetchState = FetchState.Error("Error: ${e.message}")
                    )
                }
                catch (e: Exception) {
                    auctions = "Error"
                    Log.e("AppViewModel", "Error: ${e.message}")
                    currentState.copy(
                        currentFetchState = FetchState.Error("Error: ${e.message}")
                    )
                }
            }
        }
    }


    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as DietiDealsApplication)
                val stringsRepository = application.container.stringsRepository
                val auctionsRepository = application.container.auctionsRepository
                val tagsRepository = application.container.tagsRepository
                AppViewModel(
                    stringsRepository = stringsRepository,
                    auctionsRepository = auctionsRepository,
                    tagsRepository = tagsRepository
                )
            }
        }
    }

}
