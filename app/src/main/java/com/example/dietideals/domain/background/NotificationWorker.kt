package com.example.dietideals.domain.background

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

private const val TAG = "NotificationWorker"

class NotificationWorker(
    context: Context,
    params: WorkerParameters
): Worker(context, params) {
    override fun doWork(): Result {
        return Result.success()
    }
}