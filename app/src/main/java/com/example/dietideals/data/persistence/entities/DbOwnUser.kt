package com.example.dietideals.data.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "own_users")
data class DbOwnUser(
    @PrimaryKey(autoGenerate = false)
    val username: String,
    val password: String? = null,
    val email: String,
    val firstName: String,
    val lastName: String,
    val proPicPath: String? = null,
    val bio: String,
    val nationality: String,
)
