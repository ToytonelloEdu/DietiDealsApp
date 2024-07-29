package com.example.dietideals.domain

import com.example.dietideals.data.repos.AuthRepository
import com.example.dietideals.data.repos.UsersRepository

class AuthenticationUseCase (
    private val authRepo : AuthRepository,
    private val userRepo : UsersRepository
) {

}