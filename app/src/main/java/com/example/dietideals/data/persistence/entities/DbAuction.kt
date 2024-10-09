package com.example.dietideals.data.persistence.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.domain.models.IncrementalAuction
import com.example.dietideals.domain.models.SilentAuction

@Entity(tableName = "auctions")
data class DbAuction(
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    val auctionType: String,
    val medianColor: String? = null,
    val picture: String,
    val objectName: String,
    val description: String,
    val auctioneerUsername: String? = null,
    val date: String,
    @Embedded
    val lastBid: DbLastBid? = null,
    val expirationDate: String? = null,
    val timeInterval: Int? = null,
    val startingPrice: Double? = null,
    val raisingThreshold: Double? = null,
) {

    fun toAuction(): Auction {
        return when (auctionType) {
            "SilentAuction" -> SilentAuction(this)
            "IncrementalAuction" -> IncrementalAuction(this)
            else -> throw IllegalArgumentException("Unknown auction type")
        }
    }
}
