package com.example.dietideals.data.network.serializables

import com.example.dietideals.domain.models.Auctioneer
import com.example.dietideals.domain.models.Buyer
import com.example.dietideals.domain.models.User
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
    val bio: String? = null,
    val nationality: String? = null,
    val gender: String? = null,
    val birthdate: String? = null,
    val bids: List<NetBid>? = null,
    val auctions: List<NetAuction>? = null
) {
    fun toUser(): User {
        return when(userType) {
            "Auctioneer" -> Auctioneer(this)
            "Buyer" -> Buyer(this)
            else -> throw IllegalArgumentException("NetUser type not found")
        }
    }
}