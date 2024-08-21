package com.example.dietideals.domain.models

import com.example.dietideals.data.serializables.NetUser
import java.sql.Timestamp
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Locale

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
    val bids: List<Bid>
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
        user.gender,
        user.birthdate?.let {
            Timestamp.valueOf(it.replace("Z[UTC]", "").replaceFirst("T", " "))
        }.also { if (it == null) throw IllegalArgumentException("Birthdate is null") },
        user.bids?.map { Bid(it) } ?: emptyList()
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
            bids = emptyList(),
            auctions = null,
            userType = "Buyer"
        )
    }
}