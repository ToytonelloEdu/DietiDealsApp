package com.example.dietideals.data.repos

import com.example.dietideals.ui.models.Auction
import com.example.dietideals.data.network.NetworkApiService

interface AuctionsRepository {
    suspend fun getAuctions(): List<Auction>
}

class NetworkAuctionsRepository(
    private val networkData: NetworkApiService
) : AuctionsRepository {
    override suspend fun getAuctions(): List<Auction> {
        return networkData.getAuctions().map { Auction(it.objectName) }
    }

}