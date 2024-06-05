package com.example.dietideals.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dietideals.data.AppUiState
import com.example.dietideals.data.network.ServerAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.security.cert.CertPathValidatorException

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        serverString()
    }

    private fun serverString() {
        var string: String
        viewModelScope.launch {
            string = try { ServerAPI.getString() }
            catch (e: CertPathValidatorException) { println(e.message + " " + e.cause); "Error: ${e.cause}" }
            catch (e: IOException) { println(e.message); "Error: ${e.message}" }
            _uiState.update { currentState ->
                currentState.copy(
                    serverString = string
                )
            }
        }

    }
}
