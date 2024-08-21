package com.example.dietideals.data.serializables

import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class NetUser (
    val userType: String,
    val username: String,
    val email: String,
    val password: String? = null,
    val firstName: String,
    val lastName: String,
    val proPicPath: String? = null,
    val bio: String? = null,
    val nationality: String? = null,
    val gender: String? = null,
    val birthdate: String? = null,
    val bids: List<NetBid>? = null,
    val auctions: List<NetAuction>? = null
) {

}