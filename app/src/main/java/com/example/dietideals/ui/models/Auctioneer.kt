package com.example.dietideals.ui.models

import com.example.dietideals.data.entities.NetUser

data class Auctioneer(
    val username: String,
    val email: String,
    val password: String? = null,
    val firstName: String,
    val lastName: String,
    val proPicPath: String? = null,
    val bio: String,
    val nationality: String,
) {
    constructor(user: NetUser) : this(
        user.username,
        user.email,
        user.password,
        user.firstName,
        user.lastName,
        user.proPicPath,
        user.bio,
        user.nationality
    )
}
