package com.example.dietideals.domain.models

import com.example.dietideals.data.persistence.entities.DbAuction
import com.example.dietideals.data.serializables.NetAuction
import com.example.dietideals.domain.auxiliary.Seconds
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
    val lastBid: Bid? = bids.lastOrNull(),
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
        //dbAuction.lastBid?.let { Bid(it) },
        null,
        listOf(),
        dbAuction.timeInterval!!,
        dbAuction.startingPrice!!,
        dbAuction.raisingThreshold!!
    )

    fun getLastBidOrBidsLast() : Bid? {
        return if(lastBid == null && bids.isNotEmpty()) {
            bids.lastOrNull()
        } else {
            lastBid
        }
    }

    fun calculateRemainingTime(): Seconds {
        val localLastBid = getLastBidOrBidsLast()
        if(localLastBid == null) {
            val now = Timestamp(System.currentTimeMillis())
            val remainingTime = ((date.time - now.time) / 1000).toInt() + timeInterval
            return Seconds(remainingTime)
        } else {
            val now = Timestamp(System.currentTimeMillis())
            val remainingTime = (((localLastBid.time.time) - now.time) / 1000).toInt() + timeInterval
            return Seconds(remainingTime)
        }

    }

    override fun isAuctionOver(): Boolean {
        return ! calculateRemainingTime().isTimeRemaining()
    }

    override fun hasBeenOverFor(days: Int): Boolean {
        if(isAuctionOver()) {
            val end = if (lastBid != null) {
                    Timestamp(lastBid.time.time + timeInterval * 1000)
                } else {
                    Timestamp(date.time + timeInterval * 1000)
                }
            val nowMinusDays = Timestamp(System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000))
            return end.before(nowMinusDays)
        } else
            return false
    }
}