package com.example.dietideals.domain.models

import com.example.dietideals.data.persistence.entities.DbNotification
import java.sql.Timestamp


data class Notification(
    val id: Int,
    val auction: Auction,
    val user: User?,
    val time: Timestamp,
    val notificationType: String,
    val read: Boolean,
    val received: Boolean
): Comparable<Notification> {

    fun toDbNotification(): DbNotification {
        return DbNotification(
            id,
            auction.id!!,
            time.toString(),
            notificationType,
            read,
            received
        )
    }

    override operator fun compareTo(other: Notification): Int {
        return this.time.compareTo(other.time)
    }

    override fun toString(): String {
        return when (notificationType) {
            "OWNER" -> stringForOwner()
            "WINNER" -> stringForWinner()
            "PARTICIPANT" -> stringForParticipant()
            else -> "Unknow Notification type"
        }
    }

    private fun stringForParticipant(): String {
        (auction as IncrementalAuction)
        val lastBid = auction.getLastBidOrBidsLast()
        return if(lastBid == null)
            "Your auction \"${auction.objectName}\" has finished with no offers!"
        else {
            val winner = lastBid.bidder ?: lastBid.buyer?.username ?: "Unknown"
            "Your auction \"${auction.objectName}\" has been won by $winner!"
        }
    }

    private fun stringForWinner(): String {
        (auction as IncrementalAuction)
        val lastBid = auction.getLastBidOrBidsLast()
        return if(lastBid == null)
            "The auction \"${auction.objectName}\" has finished with no offers!"
        else {
            "The auction \"${auction.objectName}\" has been won by you!"
        }
    }

    private fun stringForOwner(): String {
        (auction as IncrementalAuction)
        val lastBid = auction.getLastBidOrBidsLast()
        return if(lastBid == null)
            "The auction \"${auction.objectName}\" has finished with no offers"
        else {
            val winner = lastBid.bidder ?: lastBid.buyer?.username ?: "Unknown"
            "The auction \"${auction.objectName}\" has been won by $winner!"
        }
    }
}
