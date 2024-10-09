package com.example.dietideals.domain.background

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.dietideals.data.repos.NotificationsRepository
import com.example.dietideals.data.repos.UsersRepository
import com.example.dietideals.domain.NotificationsUseCase

class NotificationWorkerFactory(
    private val onlineNotificationsRepo: NotificationsRepository,
    private val offlineNotificationsRepo: NotificationsRepository,
    private val usersRepository: UsersRepository,
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
                    NotificationsUseCase(
                        onlineNotificationsRepo,
                        offlineNotificationsRepo,
                        usersRepository
                    )
                )
            }
            else -> null
        }
    }
}