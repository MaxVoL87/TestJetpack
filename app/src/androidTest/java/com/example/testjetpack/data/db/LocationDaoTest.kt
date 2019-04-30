package com.example.testjetpack.data.db

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testjetpack.dataflow.local.AppDatabase
import com.example.testjetpack.dataflow.local.ILocationDao
import com.example.testjetpack.mock.locations
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.KoinComponent
import org.koin.core.inject

@RunWith(AndroidJUnit4::class)
class LocationDaoTest : KoinComponent {

    private val _appDatabase: AppDatabase by inject()
    private val _locationDao: ILocationDao by lazy { _appDatabase.getLocationDao() }

    @Test
    fun checkAddUpdateRemove() {
        _locationDao.clearAll()
        MatcherAssert.assertThat("notifications empty", _locationDao.loadAll().isEmpty())

        _locationDao.insert(locations.first())
        val dbNLocations = _locationDao.loadAll()
        MatcherAssert.assertThat("location inserted", dbNLocations.size == 1)
        MatcherAssert.assertThat("location inserted correctly", dbNLocations.first() == locations.first())

        _locationDao.clearAll()

        _locationDao.insert(*locations.toTypedArray())
        MatcherAssert.assertThat("locations inserted correctly", _locationDao.loadAll().size == locations.size)

        _locationDao.clearAll()
    }

    @Test
    fun checkGetLastTrip(){
        _locationDao.clearAll()

        _locationDao.insert(locations.first().copy(startTime = 1))
        _locationDao.insert(*locations.map { it.copy(startTime = 2) }.toTypedArray())
        MatcherAssert.assertThat("locations inserted correctly", _locationDao.loadAll().size == locations.size + 1)
        MatcherAssert.assertThat("last trip got correctly", _locationDao.getLastTrip().size == locations.size)

        _locationDao.clearAll()
    }
}