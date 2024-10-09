package com.example.dietideals.domain.background

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.dietideals.data.RoomAppContainer
import com.example.dietideals.data.network.NetworkApiService
import com.example.dietideals.data.repos.NotificationsRepository
import com.example.dietideals.domain.AuthenticationUseCase
import com.example.dietideals.domain.NotificationsUseCase
import retrofit2.Retrofit

private const val TAG = "NotificationWorker"

class NotificationWorker(
    val context: Context,
    params: WorkerParameters,
    private val notificationsUseCase: NotificationsUseCase
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "NotificationWorker: doWork")
        try {
            notificationsUseCase.fetchNotifications()
            return Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching notifications", e)
            return Result.failure()
        }

    }

    private suspend fun getOfflineUsername(): String {
        val userDao = RoomAppContainer(context).usersRepository

        return userDao.getOwnUser().username
    }
}