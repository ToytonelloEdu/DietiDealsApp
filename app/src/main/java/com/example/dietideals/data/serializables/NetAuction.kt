package com.example.dietideals.data.serializables

import com.example.dietideals.domain.models.Auction
import com.example.dietideals.domain.models.IncrementalAuction
import com.example.dietideals.domain.models.SilentAuction
import kotlinx.serialization.Serializable

@Serializable
data class NetAuction(
    val id: Int? = null,
    val auctionType: String,
    val pictures: List<NetAuctionPhoto> = emptyList(),
    val medianColor: String? = null,
    val objectName: String,
    val description: String,
    val auctioneer: NetUser? = null,
    val auctioneerUsername: String? = null,
    val date: String,
    val bids: List<NetBid> = emptyList(),
    val lastBid: NetBid? = null,
    val tags: List<NetTag> = emptyList(),
    val expirationDate: String? = null,
    val acceptedBid: NetBid? = null,
    val timeInterval: Int? = null,
    val startingPrice: Double? = null,
    val raisingThreshold: Double? = null,
) {
    fun toAuction(): Auction {
        return when (auctionType) {
            "SilentAuction" -> SilentAuction(this)
            "IncrementalAuction" -> IncrementalAuction(this)
            else -> throw IllegalArgumentException("Unknown auction type")
        }
    }
}