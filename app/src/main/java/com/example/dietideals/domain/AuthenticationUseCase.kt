package com.example.dietideals.domain

import android.net.Uri
import android.util.Log
import com.example.dietideals.data.AppUiState
import com.example.dietideals.data.repos.AuthRepository
import com.example.dietideals.data.repos.UsersRepository
import com.example.dietideals.domain.auxiliary.NewUser
import com.example.dietideals.domain.auxiliary.ProfileForm
import com.example.dietideals.domain.models.Auctioneer
import com.example.dietideals.domain.models.Buyer
import com.example.dietideals.domain.models.Links
import com.example.dietideals.domain.models.User
import com.example.dietideals.ui.ProfileEditState
import com.example.dietideals.ui.ProfileFetchState
import com.example.dietideals.ui.SignUpState
import com.example.dietideals.ui.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
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
            withContext(Dispatchers.IO){ authRepo.signup(newUser.toUser()) }
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
            withContext(Dispatchers.IO) {
                authRepo.auth(handle, password).let {
                    updateUserStateByHandle(state, handle, password)
                    token = it
                    Log.i("AppViewModel", "Response: $token")
                }
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
                val savedUser: User
                withContext(Dispatchers.IO) { savedUser = offlineUsersRepo.getOwnUser() }

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

    suspend fun checkToken(state: MutableStateFlow<AppUiState>) {
        if (user != null) {
            try {
                withContext(Dispatchers.IO) {
                    authRepo.auth(user!!.username, user!!.password!!).let {
                        token = it
                    }
                }
                updateUserStateByHandle(state, user!!.username, user!!.password!!)
            }catch (http: HttpException) {
                if(http.code() == 401) {
                    logOut(state)
                } else throw http
            } catch (e: Exception) {
                Log.e("AppViewModel", "Error: ${e.message}")
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
        withContext(Dispatchers.IO) { offlineUsersRepo.deleteUser(user!!) }
        token = null
        user = null
    }

    suspend fun editUserProfile(
        state: MutableStateFlow<AppUiState>,
        profileForm: ProfileForm,
        image: suspend (Uri, String) -> Unit,
    ): Boolean {
        try {
            if (!profileForm.isValid) throw IllegalArgumentException("Invalid profile")

            val newUser = updateProfileInfos(profileForm)
            state.update { it.copy(profileEditState = ProfileEditState.Loading(profileForm)) }

            withContext(Dispatchers.IO) {
                onlineUsersRepo.updateUser(newUser, token)

                if (profileForm.photo != null)
                    image(profileForm.photo!!, newUser.username)
            }

            state.update { it.copy(profileEditState = ProfileEditState.Success(profileForm)) }
            updateUserStateByHandle(state, user!!.username, user!!.password!!)
            return true
        } catch (e: Exception) {
            Log.e("AppViewModel", "Error: ${e.message}")
            state.update {
                it.copy(profileEditState = ProfileEditState.Error(profileForm, e.message))
            }
            return false
        }
    }

    private fun updateProfileInfos(profileForm: ProfileForm) =
        when (val user = user!!) {
            is Auctioneer -> {
                user.copy(
                    links = Links(
                        profileForm.website.value,
                        profileForm.instagram.value,
                        profileForm.twitter.value,
                        profileForm.facebook.value
                    ),
                    bio = profileForm.bio.value,
                    nationality = profileForm.nationality.value,
                )
            }

            is Buyer -> {
                user.copy(
                    links = Links(
                        profileForm.website.value,
                        profileForm.instagram.value,
                        profileForm.twitter.value,
                        profileForm.facebook.value
                    ),
                    bio = profileForm.bio.value,
                    nationality = profileForm.nationality.value,
                )
            }

            else -> throw IllegalArgumentException("User type not found")
        }


    private suspend fun updateUserStateByHandle(
        state: MutableStateFlow<AppUiState>,
        handle: String,
        password: String
    ) {
        val onlineUser: User
        withContext(Dispatchers.IO) { onlineUser = onlineUsersRepo.getUserByHandle(handle) }
        when (onlineUser) {
            is Auctioneer -> {
                state.update { currentState ->
                    currentState.copy(userState = UserState.Vendor(onlineUser))
                }
                withContext(Dispatchers.IO) { offlineUsersRepo.addUser(onlineUser.copy(password = password)) }
                user = onlineUser.copy(password = password)
            }
            is Buyer -> {
                state.update { currentState ->
                    currentState.copy(userState = UserState.Bidder(onlineUser))
                }
                withContext(Dispatchers.IO) { offlineUsersRepo.addUser(onlineUser.copy(password = password)) }
                user = onlineUser.copy(password = password)
            }
            else -> throw IllegalArgumentException("NetUser type not found")
        }
    }

    suspend fun getUser(state: MutableStateFlow<AppUiState>, username: String) {
        try {
            val otherUser: User
            withContext(Dispatchers.IO) {
                otherUser = onlineUsersRepo.getUserByHandle(username)
            }
            state.update { currentState ->
                currentState.copy(otherUserState = ProfileFetchState.ProfileSuccess(otherUser))
            }
        } catch (e: Exception) {
            Log.e("AppViewModel", "Error: ${e.message}")
            state.update { currentState ->
                currentState.copy(otherUserState = ProfileFetchState.Error(e.message))
            }
        }
    }

    companion object{
        var user: User? = null
            private set

        var token: String? = null
            get() = if (field == null) null
                    else "Bearer $field"

            private set
    }
}