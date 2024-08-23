package com.example.dietideals.domain.models

import com.example.dietideals.data.persistence.entities.DbOwnUser
import com.example.dietideals.data.network.serializables.NetUser
import java.sql.Timestamp

abstract class User (
    open val username: String,
    open val email: String,
    open val password: String? = null,
    open val firstName: String,
    open val lastName: String,
    open val proPicPath: String? = null,
    open val bio: String? = null,
    open val nationality: String? = null,
    open val gender: String? = null,
    open val birthdate: Timestamp? = null
) {
    abstract fun toNetUser(): NetUser
    abstract fun toDbUser(): DbOwnUser
}