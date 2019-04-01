package com.example.testjetpack.tasks

import android.content.Context
import androidx.work.PeriodicWorkRequest
import com.example.testjetpack.tasks.workers.NotificationDownloadWorker
import java.util.concurrent.TimeUnit

class TasksFactory(private val appContext: Context) {

    fun createNotificationDownloadTask(){
        // 15 minutes is min interval
        val work = PeriodicWorkRequest.Builder(NotificationDownloadWorker::class.java, 15, TimeUnit.MINUTES).build()
        //todo: finish
    }
}