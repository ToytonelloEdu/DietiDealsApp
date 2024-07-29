package com.example.dietideals.domain.models

import com.example.dietideals.data.serializables.NetBid
import java.sql.Timestamp
import java.util.Date

data class Bid(
    val auction: Auction? = null,
    val buyer: Buyer? = null,
    val bidder: String? = null,
    val amount: Double,
    val time: Timestamp
) {
    constructor(netBid: NetBid) : this(
        netBid.auction.let { it?.toAuction() },
        netBid.buyer.let {if (it != null) Buyer(it) else null},
        netBid.bidder,
        netBid.amount,
        Timestamp.valueOf(
            netBid.time
                ?.replace("Z[UTC]", "")
                ?.replaceFirst("T", " ")
                ?: "2000-01-01 00:00:00"
        )
    )

    fun timeToDate() : Date {
        return Date.from(time.toInstant())
    }
}
