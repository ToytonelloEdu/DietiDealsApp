package com.example.dietideals

import android.app.Application
import androidx.work.Configuration
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.dietideals.data.AppContainer
import com.example.dietideals.data.RetrofitAppContainer
import com.example.dietideals.data.RoomAppContainer
import com.example.dietideals.domain.background.NotificationWorker
import com.example.dietideals.domain.background.NotificationWorkerFactory
import java.util.concurrent.TimeUnit

class DietiDealsApplication : Application() {

    lateinit var container: AppContainer
    lateinit var offlineContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = RetrofitAppContainer(this)
        offlineContainer = RoomAppContainer(this)

        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setWorkerFactory(NotificationWorkerFactory(
                    container.notificationsRepository,
                    offlineContainer.notificationsRepository,
                    offlineContainer.usersRepository
                ))
                .build()
        )

        val notifsFetchBuilder = PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
        WorkManager.getInstance(this).enqueue(notifsFetchBuilder.build())
    }
}