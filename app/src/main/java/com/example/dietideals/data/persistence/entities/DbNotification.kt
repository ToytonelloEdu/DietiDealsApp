package com.example.dietideals.data.persistence.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "notifications",
    foreignKeys = [
        ForeignKey(
            entity = DbAuction::class,
            parentColumns = ["id"],
            childColumns = ["auctionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DbNotification(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val auctionId: Int,
    val time: String,
    val notificationType: String,
    val read: Boolean,
    val received: Boolean,
) {


}