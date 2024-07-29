package com.example.dietideals.data.repos

import com.example.dietideals.data.serializables.NetUser
import com.example.dietideals.data.network.NetworkApiService

interface UsersRepository {
    suspend fun getUserByHandle(handle: String): NetUser
}

class NetworkUsersRepository(
    private val networkData : NetworkApiService
) : UsersRepository {
    override suspend fun getUserByHandle(handle: String): NetUser {
        return networkData.getUserByHandle(handle)
    }

}