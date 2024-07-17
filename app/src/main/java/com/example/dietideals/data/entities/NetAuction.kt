package com.example.dietideals.data.entities

import com.example.dietideals.ui.models.Auction
import com.example.dietideals.ui.models.IncrementalAuction
import com.example.dietideals.ui.models.SilentAuction
import kotlinx.serialization.Serializable

@Serializable
data class NetAuction(
    val id: Int? = null,
    val auctionType: String,
    val picturePath: String? = null,
    val objectName: String,
    val description: String,
    val auctioneer: NetUser? = null,
    val auctioneerUsername: String? = null,
    val date: String,
    val bids: List<NetBid> = emptyList(),
    val lastBid: NetBid? = null,
    val tags: List<NetTag> = emptyList(),
    val expirationDate: String? = null,
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