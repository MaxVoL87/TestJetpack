package com.example.testjetpack.tasks.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.own.Notification
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class NotificationDownloadWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams), KoinComponent {

    private val dataRepository: IDataRepository by inject()

    override fun doWork(): Result {
        // Download notifications
        Thread.sleep(5000)

        val curTime = Calendar.getInstance().time.time
        dataRepository.insertNotificationsIntoDB(
            listOf(
                Notification(
                    curTime.toString(),
                    "Welcome!",
                    "Snooze these notifications for: an hour, eight hours, a day, three days, or the next week. Or, turn email notifications off. For more detailed preferences, see your account page.",
                    "02.02.2019",
                    "02.02.2019"
                ),
                Notification(
                    "${curTime + 1}",
                    "Your email has been verified",
                    "Thank you for verifying your email address. Your new Upclick account has been activated and you can now login to the Merchant Area. All your account details...",
                    "20.02.2019",
                    null
                ),
                Notification(
                    "${curTime + 2}",
                    "Start your travel",
                    "This article is part of our Travel Business Startup Guide—a curated list of articles to help you plan, start, and grow your travel business!",
                    "20.04.2019",
                    null
                ),
                Notification(
                    "${curTime + 3}",
                    "Hello",
                    "Hello from the outside \nAt least I can say that I've tried \nTo tell you I'm sorry \nFor breaking your heart…",
                    "02.20.2019",
                    null
                )
            )
        )

        // Indicate whether the task finished successfully with the Result
        return Result.success()
    }
}