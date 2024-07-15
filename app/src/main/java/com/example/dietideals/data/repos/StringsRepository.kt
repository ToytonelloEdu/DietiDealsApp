package com.example.dietideals.data.repos

import com.example.dietideals.data.network.NetworkApiService

interface StringsRepository {
    suspend fun getString(): String
}

class NetworkStringsRepository(
    private val networkData: NetworkApiService
) : StringsRepository {
    override suspend fun getString(): String {
        return networkData.getString()
    }

}