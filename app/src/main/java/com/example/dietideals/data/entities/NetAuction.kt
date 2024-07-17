package com.example.dietideals.data.entities

import kotlinx.serialization.Serializable

@Serializable
data class NetAuction(
    val id: Int? = null,
    val picturePath: String? = null,
    val objectName: String,
    val description: String,
    val auctioneer: NetUser? = null,
    val auctioneerUsername: String? = null,
    val date: String,
    val bids: List<NetBid> = emptyList(),
    val lastBid: NetBid? = null,
    val tags: List<NetTag> = emptyList()
)