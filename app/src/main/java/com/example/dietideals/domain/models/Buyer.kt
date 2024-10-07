package com.example.dietideals.domain.models

import com.example.dietideals.data.persistence.entities.DbOwnUser
import com.example.dietideals.data.network.serializables.NetUser
import java.sql.Timestamp

data class Buyer(
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
    override val links: Links? = null,
    val bids: List<Bid>
): User(username, email, password, firstName, lastName, proPicPath, bio, nationality, gender, birthdate, links) {
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
        }.also { if (it == null) throw IllegalArgumentException("Birthdate is null") },
        user.links?.toLinks(),
        user.bids?.map { Bid(it) } ?: emptyList()
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
        Links(),
        emptyList()
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
            links = links?.toNetLinks(),
            bids = emptyList(),
            auctions = null,
            userType = "Buyer"
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
            userType = "Buyer"
        )
    }
}