package com.example.dietideals.data.repos

import android.util.Log
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.data.network.NetworkApiService
import com.example.dietideals.data.persistence.daos.AuctionDao

interface AuctionsRepository {
    suspend fun getAuctions(): List<Auction>
    suspend fun getAuctionById(id: Int): Auction
    suspend fun getAuctionsByUser(username: String) : List<Auction>
    suspend fun addAuction(auction: Auction, token: String? = null) : Auction
}

class NetworkAuctionsRepository(
    private val networkData: NetworkApiService
) : AuctionsRepository {

    override suspend fun getAuctions(): List<Auction> =
        networkData.getAuctions().map { it.toAuction() }

    override suspend fun getAuctionById(id: Int): Auction =
        networkData.getAuctionById(id).toAuction()

    override suspend fun getAuctionsByUser(username: String): List<Auction> =
        networkData.getAuctionsByUser(username).map { it.toAuction() }

    override suspend fun addAuction(auction: Auction, token: String?): Auction {
        return networkData.postAuction(token!!, auction.toNetAuction()).toAuction()
    }
}

class OfflineAuctionsRepository(
    private val auctionDao: AuctionDao
) : AuctionsRepository {

    override suspend fun getAuctions(): List<Auction> =
        auctionDao.getAuctions().map {
            Log.d("HomePage", "Color: ${it.medianColor}")
            it.toAuction()
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

}