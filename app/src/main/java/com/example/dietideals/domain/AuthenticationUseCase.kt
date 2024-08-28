package com.example.dietideals.domain

import android.util.Log
import com.example.dietideals.data.AppUiState
import com.example.dietideals.data.repos.AuthRepository
import com.example.dietideals.data.repos.UsersRepository
import com.example.dietideals.domain.auxiliary.NewAuction
import com.example.dietideals.domain.auxiliary.NewUser
import com.example.dietideals.domain.models.Auctioneer
import com.example.dietideals.domain.models.Buyer
import com.example.dietideals.domain.models.User
import com.example.dietideals.ui.NewAuctionState
import com.example.dietideals.ui.SignUpState
import com.example.dietideals.ui.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.HttpException

class AuthenticationUseCase (
    private val authRepo : AuthRepository,
    private val onlineUsersRepo : UsersRepository,
    private val offlineUsersRepo: UsersRepository
) {


    suspend fun signUp(state: MutableStateFlow<AppUiState>, newUser: NewUser) {
        state.update { currentState ->
            currentState.copy(signUpState = SignUpState.Loading(newUser))
        }
        try {
            if (!newUser.isValid) throw IllegalArgumentException("Invalid user")
            authRepo.signup(newUser.toUser())
            state.update { currentState ->
                currentState.copy(signUpState = SignUpState.Success(NewUser()))
            }
            logIn(state, newUser.username.value, newUser.password.value)
        } catch (e: Exception) {
            Log.e("AppViewModel", "Error: ${e.message}")
            state.update { currentState ->
                currentState.copy(signUpState = SignUpState.Error(newUser, e.message))
            }
        }

    }

    suspend fun logIn(state: MutableStateFlow<AppUiState>, handle: String, password: String) {
        try {
            authRepo.auth(handle, password).let {
                updateUserStateByHandle(state, handle, password)
                token = it
                Log.i("AppViewModel", "Response: $token")

            }
        } catch (e: HttpException) {
            Log.e("AppViewModel", "Error: ${e.message}")
            if(e.code() == 401) {
                state.update { currentState ->
                    currentState.copy(
                        userState = UserState.NotLoggedIn(true)
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("AppViewModel", "Error: ${e.message}")
        }
    }

    suspend fun rememberLogIn(state: MutableStateFlow<AppUiState>) {
            try {
                val savedUser = offlineUsersRepo.getOwnUser()
                Log.d("AppViewModel", "User: $savedUser")

                user = savedUser
                state.update { currentState ->
                    currentState.copy(
                        userState = when(savedUser) {
                            is Auctioneer -> UserState.Vendor(savedUser)
                            is Buyer -> UserState.Bidder(savedUser)
                            else -> UserState.NotLoggedIn()
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e("AppViewModel", "Error: ${e.message}")
            }
    }

    suspend fun checkRemember(state: MutableStateFlow<AppUiState>) {
        if (user != null) {
            try {
                authRepo.auth(user!!.username, user!!.password!!).let {
                    token = it
                }
            }catch (http: HttpException) {
                if(http.code() == 401) {
                    logOut(state)
                } else throw http
            } catch (e: Exception) {
                state.update { currentState ->
                    currentState.copy(
                        isOnline = false
                    )
                }
            }
        }
    }

    suspend fun logOut(state: MutableStateFlow<AppUiState>) {
        state.update { currentState ->
            currentState.copy(
                userState = UserState.NotLoggedIn()
            )
        }
        offlineUsersRepo.deleteUser(user!!)
        token = null
        user = null
    }


    private suspend fun updateUserStateByHandle(
        state: MutableStateFlow<AppUiState>,
        handle: String,
        password: String
    ) {
        val onlineUser = onlineUsersRepo.getUserByHandle(handle)
        when (onlineUser) {
            is Auctioneer -> {
                state.update { currentState ->
                    currentState.copy(
                        userState = UserState.Vendor(onlineUser)
                    )

                }
                offlineUsersRepo.addUser(onlineUser.copy(password = password))
            }
            is Buyer -> {
                state.update { currentState ->
                    currentState.copy(userState = UserState.Bidder(onlineUser))
                }
                offlineUsersRepo.addUser(onlineUser.copy(password = password))
            }
            else -> throw IllegalArgumentException("NetUser type not found")
        }
        user = onlineUser

    }

    companion object{
        var user: User? = null
            private set

        var token: String? = null
            get() =
                "Bearer $field"
            private set
    }
}