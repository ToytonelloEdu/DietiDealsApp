package com.example.dietideals.data.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "last_bids")
data class DbLastBid(
    @PrimaryKey(autoGenerate = false)
    val bidId: Int,
    val amount: Double,
    val time: String? = null
)
