package com.example.dietideals.data.repos

import android.util.Log
import com.example.dietideals.data.network.NetworkApiService
import com.example.dietideals.data.persistence.daos.NotificationDao
import com.example.dietideals.domain.models.Notification

interface NotificationsRepository {
    suspend fun getNotificationsForUser(username: String, token: String? = null): List<Notification>
    suspend fun addNotification(notification: Notification)
    suspend fun deleteNotification(notification: Notification)
}

class NetworkNotificationsRepository(
    private val networkData: NetworkApiService
) : NotificationsRepository {
    override suspend fun getNotificationsForUser(username: String, token: String?): List<Notification> {
        val notifications = networkData.getNotifications(token!!, username)
        Log.d("NetworkNotificationsRepository", "Notifications: $notifications")
        return notifications.map { it.toNotification() }
    }

    override suspend fun addNotification(notification: Notification) {
        throw NotImplementedError()
    }

    override suspend fun deleteNotification(notification: Notification) {
        throw NotImplementedError()
    }


}

class OfflineNotificationsRepository(
    private val notificationsDao: NotificationDao
): NotificationsRepository {
    override suspend fun getNotificationsForUser(username: String, token: String?): List<Notification> {
        return notificationsDao.getNotifications().map { it.toNotification() }
    }

    override suspend fun addNotification(notification: Notification) {
        return notificationsDao.insert(notification.toDbNotification())
    }

    override suspend fun deleteNotification(notification: Notification) {
        return notificationsDao.delete(notification.toDbNotification())
    }

}