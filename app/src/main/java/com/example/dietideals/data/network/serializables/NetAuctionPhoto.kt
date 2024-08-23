package com.example.dietideals.data.network.serializables

import kotlinx.serialization.Serializable

@Serializable
data class NetAuctionPhoto(
    val id: Int? = null,
    val auction: NetAuction? = null,
    val path: String
)
