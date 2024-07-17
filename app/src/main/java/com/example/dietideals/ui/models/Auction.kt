package com.example.dietideals.ui.models

import com.example.dietideals.data.entities.NetAuction
import java.sql.Timestamp

data class Auction(
    val id: Int? = null,
    val picturePath: String? = null,
    val objectName: String,
    val description: String,
    val auctioneer: Auctioneer? = null,
    val auctioneerUsername: String? = null,
    val date: Timestamp,
    val bids: MutableList<Bid>,
    var lastBid: Bid? = bids.lastOrNull(),
    val tags: List<Tag>
) {

    constructor(netAuction: NetAuction) : this(
        netAuction.id,
        netAuction.picturePath,
        netAuction.objectName,
        netAuction.description,
        netAuction.auctioneer?.let { Auctioneer(it) },
        netAuction.auctioneerUsername,
        Timestamp.valueOf(
            netAuction.date
            .replace("Z[UTC]", "")
            .replaceFirst("T", " ")
        ),
        mutableListOf(),
        null,
        netAuction.tags.map { Tag(it.tagName) }
    ) {
        this.bids.addAll(netAuction.bids.map { Bid(amount =  it.amount) })
        netAuction.lastBid?.let { this.lastBid = Bid(amount = it.amount, bidder = it.bidder) }
    }

}

