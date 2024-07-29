package com.example.dietideals.data.repos

import com.example.dietideals.data.serializables.NetAuth
import com.example.dietideals.data.serializables.NetUser
import com.example.dietideals.data.network.NetworkApiService

interface AuthRepository{
    suspend fun auth(handle: String, password: String): String //JWT Token
    suspend fun signup(netUser: NetUser): NetUser
}

class NetworkAuthRepository(
    private val networkData: NetworkApiService
) : AuthRepository {
    override suspend fun auth(handle: String, password: String): String {
        val auth = NetAuth(handle, password)
        return networkData.restLogin(auth)
    }

    override suspend fun signup(netUser: NetUser): NetUser {
        return networkData.restSignup(netUser)
    }

}