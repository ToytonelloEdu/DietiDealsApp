package com.example.dietideals.ui.models

import com.example.dietideals.data.entities.NetAuction
import java.sql.Timestamp

data class IncrementalAuction (
    override val id: Int? = null,
    override val picturePath: String? = null,
    override val objectName: String,
    override val description: String,
    override val auctioneer: Auctioneer? = null,
    override val auctioneerUsername: String? = null,
    override val date: Timestamp,
    override val bids: MutableList<Bid>,
    var lastBid: Bid? = bids.lastOrNull(),
    override val tags: List<Tag>,
    val timeInterval: Int,
    val startingPrice: Double,
    val raisingThreshold: Double,
) : Auction(id, picturePath, objectName, description, auctioneer, auctioneerUsername, date, bids, tags) {

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
        netAuction.bids.map { Bid(it) }.toMutableList(),
        netAuction.lastBid?.let { Bid(it) },
        netAuction.tags.map { Tag(it.tagName) },
        netAuction.timeInterval!!,
        netAuction.startingPrice!!,
        netAuction.raisingThreshold!!
    )
}