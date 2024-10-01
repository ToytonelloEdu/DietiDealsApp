package com.example.dietideals.data.repos

import com.example.dietideals.data.network.NetworkApiService
import com.example.dietideals.domain.models.Notification

interface NotificationsRepository {
    suspend fun getNotificationsForUser(username: String, token: String? = null): List<Notification>
}

class NetworkNotificationsRepository(
    private val networkData: NetworkApiService
) : NotificationsRepository {
    override suspend fun getNotificationsForUser(username: String, token: String?): List<Notification> =
        networkData.getNotifications(username, token!!)


}

class OfflineNotificationsRepository(
    private val notificationsDao: Notification
): NotificationsRepository {
    override suspend fun getNotificationsForUser(username: String, token: String?): List<Notification> {
        TODO("Not yet implemented")
    }

}