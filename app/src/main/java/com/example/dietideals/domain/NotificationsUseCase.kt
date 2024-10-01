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
    private val workManager = WorkManager.getInstance(context)

    fun fetchNotifications() {
        val notifsFetchBuilder = PeriodicWorkRequestBuilder<NotificationWorker>(5, TimeUnit.SECONDS)
        workManager.enqueue(notifsFetchBuilder.build())
    }
}