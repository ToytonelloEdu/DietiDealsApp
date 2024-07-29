package com.example.dietideals.data.repos

import com.example.dietideals.data.network.NetworkApiService

interface StringsRepository {
    suspend fun getString(): String
}

class NetworkStringsRepository private constructor(
    private val networkData: NetworkApiService
) : StringsRepository {
    override suspend fun getString(): String {
        return networkData.getString()
    }

    companion object {

        @Volatile private var instance: NetworkStringsRepository? = null // Volatile modifier is necessary

        fun getInstance(networkData: NetworkApiService) =
            instance ?: synchronized(this) { // synchronized to avoid concurrency problem
                instance ?: NetworkStringsRepository(networkData).also { instance = it }
            }
    }

}