package com.example.dietideals.domain.auxiliary

import android.net.Uri
import com.example.dietideals.data.serializables.NetBid
import com.example.dietideals.data.serializables.NetTag
import com.example.dietideals.data.serializables.NetUser
import com.example.dietideals.domain.models.Auctioneer
import com.example.dietideals.domain.models.Tag
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

val MAX_PHOTOS = 3

data class NewAuction(
    var auctioneer: Auctioneer? = null,
    var date: Date = Date(),

    var picturePaths: MutableList<Uri> = mutableListOf(),
    var objectName: String = "",
    var description: String = "",
    var tags: List<Tag> = mutableListOf(),
    var auctionType: AuctionType? = null,


    var expirationDate: Date? = null,

    var timeInterval: Seconds? = null,
    var startingPrice: Double? = null,
    var raisingThreshold: Double? = null,
)
