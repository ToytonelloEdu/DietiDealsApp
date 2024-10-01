package com.example.dietideals.domain.background

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.dietideals.data.repos.NotificationsRepository
import retrofit2.Retrofit

class NotificationWorkerFactory(
    private val onlineNotificationsRepo: NotificationsRepository,
    private val offlineNotificationsRepo: NotificationsRepository,
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            NotificationWorker::class.java.name -> {
                NotificationWorker(
                    appContext,
                    workerParameters,
                    onlineNotificationsRepo,
                    offlineNotificationsRepo
                )
            }
            else -> null
        }
    }
}