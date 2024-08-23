package com.example.dietideals.domain.models

import com.example.dietideals.data.persistence.entities.DbLastBid
import com.example.dietideals.data.network.serializables.NetBid
import java.sql.Timestamp
import java.util.Date

data class Bid(
    val id: Int? = null,
    val auction: Auction? = null,
    val buyer: Buyer? = null,
    val bidder: String? = null,
    val amount: Double,
    val time: Timestamp
) {
    constructor(netBid: NetBid) : this(
        netBid.id,
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

    fun toNetBid() : NetBid {
        return NetBid(
            auction = auction?.toNetAuction(),
            buyer = buyer?.toNetUser(),
            bidder = bidder,
            amount = amount,
            time = time.toString().replace(" ", "T") + "Z[UTC]"
        )
    }

    fun toDbLastBid(): DbLastBid {
        return DbLastBid(
            bidId = id!!,
            amount= amount,
            time = time.toString()
        )
    }
}
