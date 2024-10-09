package com.example.dietideals.data.network.serializables

import com.example.dietideals.domain.models.Notification
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class NetNotification(
    val id: Int,
    val auction: NetAuction,
    val user: NetUser,
    val time: String,
    val notificationType: String,
    val read: Boolean,
    val received: Boolean
) {
    fun toNotification(): Notification {
        return Notification(
            id,
            auction.toAuction(),
            user.toUser(),
            Timestamp.valueOf(
                time.replace("Z[UTC]", "")
                    .replaceFirst("T", " ")
            ),
            notificationType,
            read,
            received
        )
    }
}
