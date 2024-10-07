package com.example.dietideals.data.persistence.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.example.dietideals.domain.models.Notification
import java.sql.Timestamp

data class DbNotificationWithAuction(
    @Embedded val notification: DbNotification,
    @Relation(
        parentColumn = "auctionId",
        entityColumn = "id"
    )
    val auction: DbAuction
) {
        fun toNotification(): Notification {
        return Notification(
            notification.id,
            auction.toAuction(),
            null,
            Timestamp.valueOf(
                notification.time.replace("T", " ")
            ),
            notification.notificationType,
            notification.read,
            notification.received
        )
    }
}
