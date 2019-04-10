package com.example.testjetpack

import android.app.Application
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.testjetpack.di.*
import com.example.testjetpack.workers.NotificationDownloadWorker
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import timber.log.Timber

class MainApplication : Application() {

    private val notificationDownloadWork: PeriodicWorkRequest by inject(named<NotificationDownloadWorker>())

    override fun onCreate() {
        super.onCreate()

        // start Koin context
        startKoin {
            androidContext(this@MainApplication)
            androidLogger()
            modules(
                appModule,
                networkModule,
                databaseModule,
                gpsModule
            )
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return super.createStackElementTag(element) + "::Line:" + element.lineNumber + "::" + element.methodName + "()"
                }
            })
        }

        WorkManager.getInstance().cancelAllWork()
        WorkManager.getInstance().enqueue(notificationDownloadWork)
    }
}