package com.example.dietideals.domain.models

import java.sql.Timestamp
import java.util.Date

abstract class Auction(
    open val id: Int? = null,
    open val picturePath: String? = null,
    open val objectName: String,
    open val description: String,
    open val auctioneer: Auctioneer? = null,
    open val auctioneerUsername: String? = null,
    open val date: Timestamp,
    open val bids: MutableList<Bid>,
    open val tags: List<Tag>
) {
    fun dateToDate() : Date {
        return Date.from(date.toInstant())
    }

    abstract fun isAuctionOver() : Boolean

    abstract fun hasBeenOverFor(days: Int) : Boolean
}
