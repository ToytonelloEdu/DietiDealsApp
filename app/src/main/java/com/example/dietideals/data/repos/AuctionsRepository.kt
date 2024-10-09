package com.example.dietideals.data.repos

import android.util.Log
import com.example.dietideals.data.network.NetworkApiService
import com.example.dietideals.data.persistence.daos.AuctionDao
import com.example.dietideals.domain.auxiliary.SearchQuery
import com.example.dietideals.domain.models.Auction

interface AuctionsRepository {
    suspend fun getAuctions(): List<Auction>
    suspend fun getAuctionsQueried(query: SearchQuery): List<Auction>
    suspend fun getAuctionById(id: Int): Auction
    suspend fun getAuctionsByUser(username: String) : List<Auction>
    suspend fun addAuction(auction: Auction, token: String? = null) : Auction
    suspend fun acceptBidForAuction(auctionId: Int, bidId: Int, token: String? = null) : Auction
}

class NetworkAuctionsRepository(
    private val networkData: NetworkApiService
) : AuctionsRepository {

    override suspend fun getAuctions(): List<Auction> =
        networkData.getAuctions().map { it.toAuction() }

    override suspend fun getAuctionsQueried(query: SearchQuery): List<Auction> {
        return networkData.getAuctionsQueried(
            query.objectName.value.ifBlank { null },
            query.vendor.value.ifBlank { null },
            if (query.tags.value.size >= 1) query.tags.value[0] else null,
            if (query.tags.value.size >= 2) query.tags.value[1] else null,
            if (query.tags.value.size >= 3) query.tags.value[2] else null
        ).map { it.toAuction() }
    }

    override suspend fun getAuctionById(id: Int) =
        networkData.getAuctionById(id).toAuction()

    override suspend fun getAuctionsByUser(username: String): List<Auction> =
        networkData.getAuctionsByUser(username).map { it.toAuction() }

    override suspend fun addAuction(auction: Auction, token: String?) =
        networkData.postAuction(token!!, auction.toNetAuction()).toAuction()

    override suspend fun acceptBidForAuction(auctionId: Int, bidId: Int, token: String?) =
        networkData.acceptBid(token!!, auctionId, bidId).toAuction()
}

class OfflineAuctionsRepository(
    private val auctionDao: AuctionDao
) : AuctionsRepository {

    override suspend fun getAuctions(): List<Auction> =
        auctionDao.getAuctions().map {
            it.toAuction()
        }

    override suspend fun getAuctionsQueried(query: SearchQuery): List<Auction> {
        TODO("Not yet implemented")
    }

    override suspend fun getAuctionById(id: Int): Auction =
        auctionDao.getAuctionById(id).toAuction()

    override suspend fun getAuctionsByUser(username: String): List<Auction> {
        TODO("Not yet implemented")
    }

    override suspend fun addAuction(auction: Auction, token: String?): Auction {
        auctionDao.insert(auction.toDbAuction())
        return auction
    }

    override suspend fun acceptBidForAuction(auctionId: Int, bidId: Int, token: String?): Auction {
        TODO("Not yet implemented")
    }

}