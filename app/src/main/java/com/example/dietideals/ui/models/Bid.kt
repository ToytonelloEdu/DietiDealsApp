package com.example.dietideals.ui.models

import com.example.dietideals.data.entities.NetAuction
import com.example.dietideals.data.entities.NetBid
import java.sql.Timestamp

data class Bid(
    val auction: Auction? = null,
    val buyer: Buyer? = null,
    val bidder: String? = null,
    val amount: Double,
    val time: Timestamp? = null
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
}
