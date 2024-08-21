package com.example.dietideals.domain.auxiliary

import android.net.Uri
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.domain.models.Auctioneer
import com.example.dietideals.domain.models.IncrementalAuction
import com.example.dietideals.domain.models.SilentAuction
import com.example.dietideals.domain.models.Tag
import java.sql.Timestamp
import java.util.Date

sealed interface AuctionType{
    data object IncrementalAuction: AuctionType
    data object SilentAuction : AuctionType

    val name: String
        get() = when(this) {
                is IncrementalAuction -> "Incremental"
                is SilentAuction -> "Silent"
            }

    val index: Int
        get() = when(this) {
            is IncrementalAuction -> 0
            is SilentAuction -> 1
        }
}



data class NewAuction(
    var auctioneer: Auctioneer? = null,
    var date: Date = Date(),

    var picturePaths: MutableList<Uri> = mutableListOf(),
    var objectName: String = "",
    var description: String = "",
    var tags: MutableList<Tag> = mutableListOf(),
    var auctionType: AuctionType? = null,


    var expirationDate: Date? = null,

    var timeInterval: Seconds? = null,
    var startingPrice: Double? = null,
    var raisingThreshold: Double? = null,
) {

    fun toAuction(): Auction {
        return when (auctionType) {
            is AuctionType.IncrementalAuction -> {
                IncrementalAuction(
                    auctioneer = auctioneer!!,
                    date = Timestamp(date.time),
                    objectName = objectName,
                    description = description,
                    tags = tags,
                    bids = mutableListOf(),
                    timeInterval = timeInterval!!.toInt(),
                    startingPrice = startingPrice!!,
                    raisingThreshold = raisingThreshold!!,
                )
            }
            is AuctionType.SilentAuction -> {
                SilentAuction(
                    auctioneer = auctioneer!!,
                    date = Timestamp(date.time),
                    objectName = objectName,
                    description = description,
                    tags = tags,
                    bids = mutableListOf(),
                    expirationDate = Timestamp(expirationDate!!.time),
                )
            }
            else -> throw IllegalArgumentException("Auction type not found")
        }
    }

    companion object {
        val MAX_PHOTOS = 3
    }
}
