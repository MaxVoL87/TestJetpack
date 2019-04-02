package com.example.testjetpack.tasks

import android.content.Context
import android.os.Build
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequest
import com.example.testjetpack.tasks.workers.NotificationDownloadWorker
import java.util.concurrent.TimeUnit

class TasksFactory(private val appContext: Context) {

    fun createNotificationDownloadTask(): PeriodicWorkRequest {

        val constraints = Constraints.Builder()
            .setRequiresDeviceIdle(false) // only when device Idle
            .setRequiresCharging(false) // only when device charging
            .apply {  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) setTriggerContentMaxDelay(1, TimeUnit.MINUTES) }
            .build()

        // 15 minutes is min interval
        return PeriodicWorkRequest.Builder(NotificationDownloadWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
    }
}