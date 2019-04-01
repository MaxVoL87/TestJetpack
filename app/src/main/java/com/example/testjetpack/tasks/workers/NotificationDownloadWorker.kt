package com.example.testjetpack.tasks.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationDownloadWorker (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // Do the work here--in this case, upload the images.


        // Indicate whether the task finished successfully with the Result
        return Result.success()
    }
}