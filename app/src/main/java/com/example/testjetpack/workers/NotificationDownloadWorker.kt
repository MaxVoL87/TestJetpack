package com.example.testjetpack.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.mock.notifications
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class NotificationDownloadWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams), KoinComponent {

    private val dataRepository: IDataRepository by inject()

    override val coroutineContext = Dispatchers.IO

    override suspend fun doWork(): Result {
        // Download notifications
        delay(5000)

        var curTime = Calendar.getInstance().time.time
        dataRepository.insertNotificationsIntoDB(notifications.map { it.copy(id = curTime++.toString()) })

        // Indicate whether the task finished successfully with the Result
        return Result.success()
    }
}