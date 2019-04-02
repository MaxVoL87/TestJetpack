package com.example.testjetpack.dataflow.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testjetpack.models.gps.Location

@Dao
interface ILocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg location: Location)

    @Query("DELETE FROM location_table")
    fun clearAll()
}