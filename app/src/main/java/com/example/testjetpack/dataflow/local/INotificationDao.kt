package com.example.testjetpack.dataflow.local

import androidx.room.*
import com.example.testjetpack.models.own.Notification

@Dao
interface INotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotifications(vararg notifications: Notification)

    @Update
    fun updateNotifications(vararg notifications: Notification)

    @Delete
    fun deleteNotifications(vararg notifications: Notification)

    @Query("SELECT * FROM notification_table")
    fun loadAllNotifications(): List<Notification>

    @Query("SELECT * FROM notification_table ORDER BY id ASC")
    fun loadAllNotificationsByID(): List<Notification>
}