package com.example.dietideals.domain.models

import com.example.dietideals.data.persistence.entities.DbAuction
import com.example.dietideals.data.serializables.NetAuction
import java.sql.Timestamp
import java.util.Date

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

    constructor(dbAuction: DbAuction) : this(
        dbAuction.id,
        dbAuction.picturePath,
        dbAuction.objectName,
        dbAuction.description,
        null,
        dbAuction.auctioneerUsername,
        Timestamp.valueOf(
            dbAuction.date
                .replace("Z[UTC]", "")
                .replaceFirst("T", " ")
        ),
        mutableListOf(),
        //dbAuction.tags.map { Tag(it) },
        emptyList(),
        Timestamp.valueOf(
            dbAuction.expirationDate!!
                .replace("Z[UTC]", "")
                .replaceFirst("T", " ")
        )
    )

    fun expirationDateToDate() : Date {
        return Date.from(expirationDate.toInstant())
    }

    override fun isAuctionOver() : Boolean {
        return Timestamp(System.currentTimeMillis()) >= expirationDate
    }

    override fun hasBeenOverFor(days: Int): Boolean {
        if(isAuctionOver()) {
            val nowMinusDays = Timestamp(System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000))
            return expirationDate.before(nowMinusDays)
        } else
            return false
    }
}