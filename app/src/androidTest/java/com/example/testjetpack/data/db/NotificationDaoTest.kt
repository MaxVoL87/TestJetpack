package com.example.testjetpack.data.db

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testjetpack.dataflow.local.AppDatabase
import com.example.testjetpack.dataflow.local.INotificationDao
import com.example.testjetpack.mock.notifications
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.KoinComponent
import org.koin.core.inject

@RunWith(AndroidJUnit4::class)
class NotificationDaoTest : KoinComponent {

    private val appDatabase: AppDatabase by inject()
    private val notificationDao: INotificationDao
        get() {
           return appDatabase.getNotificationDao()
        }

    @Test
    fun checkAddUpdateRemove() {
        notificationDao.clearAll()
        assertThat("notifications empty", notificationDao.loadAll().isEmpty())

        notificationDao.insert(notifications.first())
        val dbNotifications = notificationDao.loadAll()
        assertThat("notification inserted", dbNotifications.size == 1)
        assertThat("notification inserted correctly", dbNotifications.first() == notifications.first())

        val mNotif = dbNotifications.first().copy(text = "test")
        notificationDao.update(mNotif)
        assertThat("notification updated correctly", notificationDao.loadAll().first() == mNotif)

        notificationDao.delete(mNotif)
        assertThat("notification deleted", notificationDao.loadAll().isEmpty())

        notificationDao.insert(*notifications.toTypedArray())
        assertThat("notifications inserted correctly", notificationDao.loadAll().size == notifications.size)

        notificationDao.clearAll()
    }

    @Test
    fun checkLoadById(){
        notificationDao.clearAll()

        notificationDao.insert(*notifications.toTypedArray())

        val dbNotifications = notificationDao.loadAllByID()
        val isSortedById = dbNotifications.zip(dbNotifications.drop(1)).all { (a, b) -> a.id <= b.id } // check notifications was sorted by id
        assertThat("notifications inserted correctly", isSortedById)

        notificationDao.clearAll()
    }

}