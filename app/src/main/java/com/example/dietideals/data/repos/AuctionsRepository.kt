package com.example.dietideals.data.repos

import com.example.dietideals.ui.models.Auction
import com.example.dietideals.data.network.NetworkApiService
import com.example.dietideals.ui.models.IncrementalAuction
import com.example.dietideals.ui.models.SilentAuction

interface AuctionsRepository {
    suspend fun getAuctions(): List<Auction>
    suspend fun getAuctionById(id: Int): Auction
}

class NetworkAuctionsRepository(
    private val networkData: NetworkApiService
) : AuctionsRepository {
    override suspend fun getAuctions(): List<Auction> {
        return networkData.getAuctions().map {
                it.toAuction()
        }
    }

    override suspend fun getAuctionById(id: Int): Auction {
        return networkData.getAuctionById(id).toAuction()
    }

}