package com.example.dietideals.data.repos

import com.example.dietideals.ui.models.Auction
import com.example.dietideals.data.network.NetworkApiService

interface AuctionsRepository {
    suspend fun getAuctions(): List<Auction>
    suspend fun getAuctionById(id: Int): Auction
}

class NetworkAuctionsRepository(
    private val networkData: NetworkApiService
) : AuctionsRepository {
    override suspend fun getAuctions(): List<Auction> {
        return networkData.getAuctions().map {
            Auction(it)
        }
    }

    override suspend fun getAuctionById(id: Int): Auction {
        return Auction(networkData.getAuctionById(id))
    }

}