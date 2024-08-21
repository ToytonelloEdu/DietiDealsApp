package com.example.dietideals.data.repos

import com.example.dietideals.data.network.NetworkApiService
import com.example.dietideals.domain.models.Bid

interface BidsRepository {
    suspend fun postBid(bid: Bid, token: String? = null): Bid
    suspend fun getBidsByUser(username: String): List<Bid>
}

class NetworkBidsRepository(
    private val networkData: NetworkApiService
) : BidsRepository {
    override suspend fun postBid(bid: Bid, token: String?): Bid =
        networkData.postBid(token!!, bid.toNetBid()).let { Bid(it) }

    override suspend fun getBidsByUser(username: String): List<Bid> =
        networkData.getBidsByUser(username).map { Bid(it) }

}