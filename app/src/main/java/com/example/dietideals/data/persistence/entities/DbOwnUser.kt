package com.example.dietideals.data.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dietideals.domain.models.Auctioneer
import com.example.dietideals.domain.models.Buyer
import com.example.dietideals.domain.models.User

@Entity(tableName = "own_users")
data class DbOwnUser(
    @PrimaryKey(autoGenerate = false)
    val username: String,
    val password: String? = null,
    val email: String,
    val firstName: String,
    val lastName: String,
    val proPicPath: String? = null,
    val bio: String? = null,
    val nationality: String? = null,
    val gender: String? = null,
    val birthdate: String? = null,
    val userType: String
) {
    fun toUser(): User {
        return when (userType) {
            "Auctioneer" -> Auctioneer(this)
            "Buyer" -> Buyer(this)
            else -> throw IllegalArgumentException()
        }
    }
}
