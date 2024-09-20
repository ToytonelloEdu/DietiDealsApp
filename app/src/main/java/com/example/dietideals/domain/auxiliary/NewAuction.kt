package com.example.dietideals.domain.auxiliary

import android.net.Uri
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.domain.models.Auctioneer
import com.example.dietideals.domain.models.IncrementalAuction
import com.example.dietideals.domain.models.SilentAuction
import com.example.dietideals.domain.models.Tag
import com.example.dietideals.ui.NewAuctionState
import java.sql.Timestamp
import java.util.Date

sealed interface AuctionType{
    data object IncrementalAuction: AuctionType
    data object SilentAuction : AuctionType

    val name: String
        get() = when(this) {
                is IncrementalAuction -> "Incremental"
                is SilentAuction -> "Silent"
            }

    val index: Int
        get() = when(this) {
            is IncrementalAuction -> 0
            is SilentAuction -> 1
        }
}


data class NewAuction(
    val auctioneer: FormField<Auctioneer?> =
        FormField(null) { if (it == null) throw IllegalArgumentException("Auctioneer cannot be null") ;true },
    val date: FormField<Date> =
        FormField(Date()) {
            if (it.time < (System.currentTimeMillis() - (60 * 1000))) throw IllegalArgumentException("Date cannot be in the past")
            true
        },

    var picturePaths: MutableList<Uri> = mutableListOf(),
    val objectName: FormField<String> =
        FormField("") { if (it.isBlank()) throw IllegalArgumentException("Object name cannot be empty"); true },
    val description: FormField<String> =
        FormField("") { if (it.isBlank()) throw IllegalArgumentException("Description cannot be empty"); true },
    var tags: MutableList<Tag> = mutableListOf(),
    val auctionType: FormField<AuctionType?> =
        FormField(null) { if (it == null) throw IllegalArgumentException("Choose an Auction type"); true },


    val expirationDate: FormField<Date?> =
        FormField(null) {
            if(it == null) throw IllegalArgumentException("Expiration date is not set or invalid")
            if(it.time < (System.currentTimeMillis() + (24*60*60*1000))) throw IllegalArgumentException("Expiration date has to be at least a day from now")
            true
        },

    val timeInterval: FormField<Seconds?> =
        FormField(null) {
            if(it == null) throw IllegalArgumentException("Time interval is not set or invalid")
            if(it < 3600) throw IllegalArgumentException("Time interval has to be at least an hour")
            true
        },
    val startingPrice: FormField<Double?> =
        FormField(null) {
            if(it == null) throw IllegalArgumentException("Starting price is not set or invalid")
            if(it < 1) throw IllegalArgumentException("Starting price has to be at least 1")
            true
        },
    val raisingThreshold: FormField<Double?> =
        FormField(null) {
            if(it == null) throw IllegalArgumentException("Raising threshold is not set or invalid")
            if(it < 1) throw IllegalArgumentException("Raising threshold has to be at least 1")
            true
        }
): Form {

    fun toAuction(): Auction {
        return when (auctionType.value) {
            is AuctionType.IncrementalAuction -> {
                IncrementalAuction(
                    auctioneer = auctioneer.value!!,
                    date = Timestamp(date.value.time),
                    objectName = objectName.value,
                    description = description.value,
                    tags = tags,
                    bids = mutableListOf(),
                    timeInterval = timeInterval.value!!.toInt(),
                    startingPrice = startingPrice.value!!,
                    raisingThreshold = raisingThreshold.value!!,
                )
            }
            is AuctionType.SilentAuction -> {
                SilentAuction(
                    auctioneer = auctioneer.value!!,
                    date = Timestamp(date.value.time),
                    objectName = objectName.value,
                    description = description.value,
                    tags = tags,
                    bids = mutableListOf(),
                    expirationDate = Timestamp(expirationDate.value!!.time),
                )
            }
            else -> throw IllegalArgumentException("Auction type not found")
        }
    }

    companion object {
        const val MAX_PHOTOS = 3
    }

    override val isValid: Boolean
        get() {
            val baseAuction =
                    auctioneer.isValid &&
                    date.isValid &&
                    objectName.isValid &&
                    description.isValid &&
                    auctionType.isValid

            return baseAuction &&
                    when (auctionType.value) {
                        is AuctionType.IncrementalAuction -> {
                            timeInterval.isValid &&
                                    startingPrice.isValid &&
                                    raisingThreshold.isValid
                        }
                        is AuctionType.SilentAuction -> {
                            expirationDate.isValid
                        }
                        else -> throw IllegalArgumentException("Auction type not found")
                    }
        }
}
