package com.example.testjetpack.dataflow.repository

import com.example.testjetpack.dataflow.local.AppDatabase
import com.example.testjetpack.dataflow.network.IGitApi
import com.example.testjetpack.models.Notification
import com.example.testjetpack.models.Profile
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val gitApi: IGitApi,
    private val appDatabase: AppDatabase
) : IDataRepository {

    override fun getProfileAsync(): Deferred<Profile> {
        //todo: change with api
        return GlobalScope.async {
            Profile(
                "https://picsum.photos/200/200/?random",
                "Benjamin",
                "Bankog",
                "11.07.1975",
                "MORGA753116SM9IJ",
                "MQQ346789C"
            )
        }
    }

    override fun getNotificationsAsync(): Deferred<List<Notification>> {
        //todo: change with api
        return GlobalScope.async {
            listOf(
                Notification(
                    "00001",
                    "Welcome!",
                    "Snooze these notifications for: an hour, eight hours, a day, three days, or the next week. Or, turn email notifications off. For more detailed preferences, see your account page.",
                    "02.02.2019",
                    "02.02.2019"
                ),
                Notification(
                    "00002",
                    "Your email has been verified",
                    "Thank you for verifying your email address. Your new Upclick account has been activated and you can now login to the Merchant Area. All your account details...",
                    "20.02.2019",
                    null
                ),
                Notification(
                    "00003",
                    "Start your travel",
                    "This article is part of our Travel Business Startup Guide—a curated list of articles to help you plan, start, and grow your travel business!",
                    "20.04.2019",
                    null
                ),
                Notification(
                    "00004",
                    "Hello",
                    "Hello from the outside \nAt least I can say that I've tried \nTo tell you I'm sorry \nFor breaking your heart…",
                    "02.20.2019",
                    null
                )
            )
        }
    }


    companion object {
        private const val DEFAULT_NETWORK_PAGE_SIZE = 10
    }
}