package com.example.testjetpack.dataflow.local

import androidx.room.*
import com.example.testjetpack.models.own.Notification

@Dao
interface INotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg notifications: Notification)

    @Update
    fun update(vararg notifications: Notification)

    @Delete
    fun delete(vararg notifications: Notification)

    @Query("DELETE FROM notification_table")
    fun clearAll()

    @Query("SELECT * FROM notification_table")
    fun loadAll(): List<Notification>

    @Query("SELECT * FROM notification_table ORDER BY id ASC")
    fun loadAllByID(): List<Notification>
}