package com.example.dietideals.domain.models

import androidx.annotation.IntRange
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.dietideals.data.persistence.entities.DbAuction
import com.example.dietideals.data.network.serializables.NetAuction
import org.jetbrains.annotations.Range
import java.sql.Timestamp
import java.util.Date

data class SilentAuction (
    override val id: Int? = null,
    override val pictures: List<String> = emptyList(),
    override val medianColor: Color? = null,
    override val objectName: String,
    override val description: String,
    override val auctioneer: Auctioneer? = null,
    override val auctioneerUsername: String? = null,
    override val date: Timestamp,
    override val bids: MutableList<Bid>,
    override val tags: List<Tag>,
    val expirationDate: Timestamp
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
        netAuction.tags.map { Tag(it.tagName) },
        Timestamp.valueOf(
            netAuction.expirationDate!!
                .replace("Z[UTC]", "")
                .replaceFirst("T", " ")
        )
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

    override fun hasBeenOverFor(@IntRange(from = 0, to = 14) days: Int): Boolean {
        if(days < 0 || days > 14) throw IllegalArgumentException("Days has to be between 0 and 14")
        if(isAuctionOver()) {
            val nowMinusDays = Timestamp(System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000))
            return expirationDate.before(nowMinusDays)
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
            tags = tags.map { it.toNetTag() },
            expirationDate = expirationDate.toString().replace(" ", "T") + "Z[UTC]",
            auctionType = "SilentAuction"
        )
    }

    override fun toDbAuction(): DbAuction {
        return DbAuction(
            id = id!!,
            picture = pictures.firstOrNull() ?: "no_picture",
            medianColor = medianColor?.toArgb()?.let{Integer.toHexString(it)},
            objectName = objectName,
            description = description,
            auctioneerUsername = auctioneerUsername,
            date = date.toString().replace(" ", "T") + "Z[UTC]",
            expirationDate = expirationDate.toString().replace(" ", "T") + "Z[UTC]",
            auctionType = "SilentAuction"
        )
    }
}