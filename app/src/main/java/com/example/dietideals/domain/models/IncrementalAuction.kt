package com.example.dietideals.domain.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.dietideals.data.persistence.entities.DbAuction
import com.example.dietideals.data.network.serializables.NetAuction
import com.example.dietideals.domain.auxiliary.Seconds
import java.sql.Timestamp

data class IncrementalAuction (
    override val id: Int? = null,
    override val pictures: List<String> = emptyList(),
    override val medianColor: Color? = null,
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
) : Auction(id, pictures, medianColor, objectName, description, auctioneer, auctioneerUsername, date, bids, tags) {

    constructor(netAuction: NetAuction) : this(
        netAuction.id,
        netAuction.pictures.map { it.path },
        netAuction.medianColor?.let { Color(it.toLong(radix = 16)) },
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
        listOf(dbAuction.picture),
        dbAuction.medianColor?.let { Color(it.toLong(radix = 16)) },
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

    fun calculateNextAmount() : Double {
        val lastBid = getLastBidOrBidsLast()
        return (((lastBid?.amount?.plus(raisingThreshold)) ?: startingPrice))
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

    override fun toNetAuction(): NetAuction {
        return NetAuction(
            id = id,
            pictures = emptyList(),
            objectName = objectName,
            description = description,
            auctioneer = auctioneer?.toNetUser(),
            auctioneerUsername = auctioneerUsername,
            date = date.toString().replace(" ", "T") + "Z[UTC]",
            bids = emptyList(),
            lastBid = null,
            tags = tags.map { it.toNetTag() },
            timeInterval = timeInterval,
            startingPrice = startingPrice,
            raisingThreshold = raisingThreshold,
            auctionType = "IncrementalAuction"
        )
    }

    override fun toDbAuction(): DbAuction {
        return DbAuction(
            id = id!!,
            picture = pictures.first(),
            medianColor = medianColor?.toArgb()?.let{Integer.toHexString(it)},
            objectName = objectName,
            description = description,
            auctioneerUsername = auctioneerUsername,
            date = date.toString().replace(" ", "T") + "Z[UTC]",
            lastBid = getLastBidOrBidsLast()?.toDbLastBid(),
            timeInterval = timeInterval,
            startingPrice = startingPrice,
            raisingThreshold = raisingThreshold,
            auctionType = "IncrementalAuction"
        )
    }
}