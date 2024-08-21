package com.example.dietideals.domain

import android.util.Log
import com.example.dietideals.data.repos.AuthRepository
import com.example.dietideals.data.repos.UsersRepository
import com.example.dietideals.domain.auxiliary.NewUser

class AuthenticationUseCase (
    private val authRepo : AuthRepository,
    private val userRepo : UsersRepository
) {
    suspend fun signUp(newUser: NewUser) {
        try {
            val netUser = authRepo.signup(newUser.toUser())
        } catch (e: Exception) {
            Log.e("AppViewModel", "Error: ${e.message}")
        }

    }

}