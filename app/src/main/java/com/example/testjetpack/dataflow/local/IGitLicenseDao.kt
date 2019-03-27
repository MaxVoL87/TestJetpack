package com.example.testjetpack.dataflow.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testjetpack.models.git.db.License

@Dao
interface IGitLicenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg license: License)

    @Query("DELETE FROM license_table")
    fun clearAll()

    @Query("SELECT * FROM license_table")
    fun getAll(): List<License>

    @Query("SELECT * FROM license_table WHERE name = :name")
    fun getLicenseByName(name: String): License
}