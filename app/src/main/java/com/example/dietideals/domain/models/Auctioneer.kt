package com.example.dietideals.domain.models

import com.example.dietideals.data.persistence.entities.DbOwnUser
import com.example.dietideals.data.network.serializables.NetUser
import java.sql.Timestamp

data class Auctioneer(
    override val username: String,
    override val email: String,
    override val password: String? = null,
    override val firstName: String,
    override val lastName: String,
    override val proPicPath: String? = null,
    override val bio: String? = null,
    override val nationality: String? = null,
    override val gender: String? = null,
    override val birthdate: Timestamp? = null,
    val auctions: MutableList<Auction>
): User(username, email, password, firstName, lastName, proPicPath, bio, nationality, gender, birthdate) {
    constructor(user: NetUser) : this(
        user.username,
        user.email,
        user.password,
        user.firstName,
        user.lastName,
        user.proPicPath,
        user.bio,
        user.nationality,
        user.gender,
        user.birthdate?.let {
            Timestamp.valueOf(it.replace("Z[UTC]", "").replaceFirst("T", " "))
        },
        user.auctions?.map { it.toAuction() }?.toMutableList() ?: mutableListOf()
    )

    constructor(user: DbOwnUser) : this(
        user.username,
        user.email,
        user.password,
        user.firstName,
        user.lastName,
        user.proPicPath,
        user.bio,
        user.nationality,
        user.gender,
        user.birthdate?.let {
            Timestamp.valueOf(it.replace("Z[UTC]", "").replaceFirst("T", " "))
        }.also { if (it == null) throw IllegalArgumentException("Birthdate is null") },
        mutableListOf()
    )

    override fun toNetUser(): NetUser {
        return NetUser(
            username = username,
            email = email,
            password = password,
            firstName = firstName,
            lastName = lastName,
            proPicPath = proPicPath,
            bio = bio,
            nationality = nationality,
            gender = gender,
            birthdate = birthdate.toString().replace(" ", "T") + "Z[UTC]",
            bids = null,
            auctions = emptyList(),
            userType = "Auctioneer"
        )
    }

    override fun toDbUser(): DbOwnUser {
        return DbOwnUser(
            username = username,
            email = email,
            password = password,
            firstName = firstName,
            lastName = lastName,
            proPicPath = proPicPath,
            bio = bio,
            nationality = nationality,
            gender = gender,
            birthdate = birthdate.toString().replace(" ", "T") + "Z[UTC]",
            userType = "Auctioneer"
        )
    }
}
