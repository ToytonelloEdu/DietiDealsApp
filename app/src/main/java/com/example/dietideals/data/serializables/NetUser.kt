package com.example.dietideals.data.serializables

import kotlinx.serialization.Serializable

@Serializable
data class NetUser (
    val userType: String,
    val username: String,
    val email: String,
    val password: String? = null,
    val firstName: String,
    val lastName: String,
    val proPicPath: String? = null,
    val bio: String,
    val nationality: String,
    val bids: List<NetBid>? = null,
    val auctions: List<NetAuction>? = null
) {

}