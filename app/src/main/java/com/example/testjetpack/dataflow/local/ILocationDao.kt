package com.example.testjetpack.dataflow.local

import androidx.room.*
import com.example.testjetpack.models.own.Location

@Dao
interface ILocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg location: Location)

    @Query("DELETE FROM location_table")
    fun clearAll()

    @Transaction
    @Query("SELECT * FROM location_table WHERE start_time = (SELECT max(start_time) FROM location_table) ORDER BY id ASC")
    fun getLastTrip(): List<Location>
}