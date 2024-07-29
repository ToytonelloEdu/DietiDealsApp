package com.example.dietideals.data.serializables

import kotlinx.serialization.Serializable

@Serializable
data class NetBid (
    val id: Int? = null,
    val auction: NetAuction? = null,
    val buyer: NetUser? = null,
    val bidder: String? = null,
    val amount: Double,
    val time: String? = null
) {
}