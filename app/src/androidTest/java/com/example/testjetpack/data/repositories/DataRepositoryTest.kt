package com.example.testjetpack.data.repositories

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testjetpack.dataflow.local.AppDatabase
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.mock.locations
import com.example.testjetpack.mock.notifications
import org.junit.Assert.assertTrue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.KoinComponent
import org.koin.core.inject

@RunWith(AndroidJUnit4::class)
class DataRepositoryTest : KoinComponent {

    private val appDatabase: AppDatabase by inject()
    private val dataRepository: IDataRepository by inject()

    @Test
    fun checkNotifications() {
        clearAllData()
        assertThat("notifications cleared", dataRepository.getNotifications().isEmpty())

        dataRepository.insertNotificationsIntoDB(listOf(notifications.first()))
        var dbNotifications = dataRepository.getNotifications()
        assertThat("notification added", dbNotifications.size == 1)
        assertTrue(dbNotifications.first() == notifications.first())

        dataRepository.insertNotificationsIntoDB(listOf(notifications[1]))
        dbNotifications = dataRepository.getNotifications()
        assertTrue(dbNotifications.size == 2)

        // check notification replaced
        dataRepository.insertNotificationsIntoDB(listOf(notifications[1]))
        dbNotifications = dataRepository.getNotifications()
        assertThat("notification replaced", dbNotifications.size == 2)
        assertTrue(dbNotifications[1] == notifications[1])

        clearAllData()
        assertThat("notifications cleared", dataRepository.getNotifications().isEmpty())

        dataRepository.insertNotificationsIntoDB(notifications)
        assertTrue(dataRepository.getNotifications().size == notifications.size)

        clearAllData()
    }

    @Test
    fun checkLocations() {
        dataRepository.removeAllLocationsFromDB()
        //todo: no method to get locations

        // check all locations added
        dataRepository.insertLocationsIntoDB(locations)
        val trip = dataRepository.getTrip()
        assertTrue(trip.locations.size == locations.size)

        // check locations added correctly
        val isSame = trip.locations.map { locations.contains(it) }.all { it }
        assertTrue(isSame)

        // check locations id incremented with the same location (not replace)
        dataRepository.insertLocationsIntoDB(listOf(locations.first()))
        assertTrue(dataRepository.getTrip().locations.size == locations.size + 1)

        dataRepository.removeAllLocationsFromDB()
    }

    private fun clearAllData() {
        appDatabase.clearAllData()
    }
}