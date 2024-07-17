package com.example.dietideals.ui.models

import com.example.dietideals.data.entities.NetAuction
import java.sql.Timestamp

data class SilentAuction (
    override val id: Int? = null,
    override val picturePath: String? = null,
    override val objectName: String,
    override val description: String,
    override val auctioneer: Auctioneer? = null,
    override val auctioneerUsername: String? = null,
    override val date: Timestamp,
    override val bids: MutableList<Bid>,
    override val tags: List<Tag>,
    val expirationDate: Timestamp
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
        netAuction.tags.map { Tag(it.tagName) },
        Timestamp.valueOf(
            netAuction.expirationDate!!
                .replace("Z[UTC]", "")
                .replaceFirst("T", " ")
        )
    )
}