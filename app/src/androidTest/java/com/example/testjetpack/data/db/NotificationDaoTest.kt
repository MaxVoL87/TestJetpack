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

    private val _appDatabase: AppDatabase by inject()
    private val _notificationDao: INotificationDao by lazy { _appDatabase.getNotificationDao() }

    @Test
    fun checkAddUpdateRemove() {
        _notificationDao.clearAll()
        assertThat("notifications empty", _notificationDao.loadAll().isEmpty())

        _notificationDao.insert(notifications.first())
        val dbNotifications = _notificationDao.loadAll()
        assertThat("notification inserted", dbNotifications.size == 1)
        assertThat("notification inserted correctly", dbNotifications.first() == notifications.first())

        val mNotif = dbNotifications.first().copy(text = "test")
        _notificationDao.update(mNotif)
        assertThat("notification updated correctly", _notificationDao.loadAll().first() == mNotif)

        _notificationDao.delete(mNotif)
        assertThat("notification deleted", _notificationDao.loadAll().isEmpty())

        _notificationDao.insert(*notifications.toTypedArray())
        assertThat("notifications inserted correctly", _notificationDao.loadAll().size == notifications.size)

        _notificationDao.clearAll()
    }

    @Test
    fun checkLoadById(){
        _notificationDao.clearAll()

        _notificationDao.insert(*notifications.toTypedArray())

        val dbNotifications = _notificationDao.loadAllByID()
        val isSortedById = dbNotifications.zip(dbNotifications.drop(1)).all { (a, b) -> a.id <= b.id } // check notifications was sorted by id
        assertThat("notifications inserted correctly", isSortedById)

        _notificationDao.clearAll()
    }

}