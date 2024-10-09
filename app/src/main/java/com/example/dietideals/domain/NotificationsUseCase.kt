package com.example.dietideals.domain

import android.util.Log
import com.example.dietideals.data.AppUiState
import com.example.dietideals.data.repos.NotificationsRepository
import com.example.dietideals.data.repos.OfflineUsersRepository
import com.example.dietideals.data.repos.UsersRepository
import com.example.dietideals.domain.models.Notification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.HttpException

class NotificationsUseCase(
    private val onlineNotificationsRepo: NotificationsRepository,
    private val offlineNotificationsRepo: NotificationsRepository,
    private val offlineUsersRepository: UsersRepository
) {

    private var username: String? = null

    suspend fun fetchNotifications() {
        try {
            onlineNotificationsRepo.getNotificationsForUser(
                fetchUsername(), "Bearer token"
            ).forEach {
                offlineNotificationsRepo.addNotification(it)
                notifications.add(it)
                Log.d("NotificationsUseCase", "Notification: $it")
            }
            Log.d("NotificationsUseCase", "Notifications: $notifications")
        } catch (e: Exception) {
            Log.e("NotificationsUseCase", "Error fetching notifications", e)
        }
    }

    private suspend fun fetchUsername(): String {
        if (username == null) username = offlineUsersRepository.getOwnUser().username
        Log.d("NotificationsUseCase", "Username: $username")
        return username!!
    }

    suspend fun refreshNotifications(state: MutableStateFlow<AppUiState>) {
        try {
            fetchNotifications()
            state.update {
                it.copy(notifications = notifications)
            }
        } catch (http: HttpException) {
            Log.e("NotificationsUseCase", "Server error fetching notifications", http)
        } catch (e: Exception) {
            Log.e("NotificationsUseCase", "Unknown error fetching notifications", e)
        }

    }

    companion object {

        val notifications = sortedSetOf<Notification>()

    }
}