package com.example.dietideals.domain

import android.app.Notification
import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.dietideals.domain.background.NotificationWorker
import com.example.dietideals.domain.models.User
import java.util.concurrent.TimeUnit

class NotificationsUseCase(
    context: Context
) {
    val workManager = WorkManager.getInstance(context)

    fun fetchNotifications(user: User): List<Notification> {
        val notifsFetchBuilder = PeriodicWorkRequestBuilder<NotificationWorker>(5, TimeUnit.SECONDS)

        return listOf()
    }
}