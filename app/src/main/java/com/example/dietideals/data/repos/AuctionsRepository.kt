package com.example.dietideals.data.repos

import com.example.dietideals.domain.models.Auction
import com.example.dietideals.data.network.NetworkApiService
import com.example.dietideals.data.persistence.daos.AuctionDao

interface AuctionsRepository {
    suspend fun getAuctions(): List<Auction>
    suspend fun getAuctionById(id: Int): Auction
}

class NetworkAuctionsRepository(
    private val networkData: NetworkApiService
) : AuctionsRepository {

    override suspend fun getAuctions(): List<Auction> =
        networkData.getAuctions().map { it.toAuction() }

    override suspend fun getAuctionById(id: Int): Auction =
        networkData.getAuctionById(id).toAuction()
}

class OfflineAuctionsRepository(
    private val auctionDao: AuctionDao
) : AuctionsRepository {

    override suspend fun getAuctions(): List<Auction> =
        auctionDao.getAuctions().map { it.toAuction() }

    override suspend fun getAuctionById(id: Int): Auction =
        auctionDao.getAuctionById(id).toAuction()

}