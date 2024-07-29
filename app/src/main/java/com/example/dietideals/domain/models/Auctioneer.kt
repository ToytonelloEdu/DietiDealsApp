package com.example.dietideals.domain.models

import com.example.dietideals.data.serializables.NetUser

data class Auctioneer(
    override val username: String,
    override val email: String,
    override val password: String? = null,
    override val firstName: String,
    override val lastName: String,
    override val proPicPath: String? = null,
    override val bio: String,
    override val nationality: String,
    val auctions: List<Auction>
): User(username, email, password, firstName, lastName, proPicPath, bio, nationality) {
    constructor(user: NetUser) : this(
        user.username,
        user.email,
        user.password,
        user.firstName,
        user.lastName,
        user.proPicPath,
        user.bio,
        user.nationality,
        user.auctions?.map { it.toAuction() } ?: emptyList()
    )
}
