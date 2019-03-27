package com.example.testjetpack.dataflow.local

import androidx.paging.DataSource
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
    fun loadAllNotifications(): DataSource.Factory<Int, Notification>

    // The Int type parameter tells Room to use a PositionalDataSource
    // object, with position-based loading under the hood.
    @Query("SELECT * FROM notification_table ORDER BY date_of_creation DESC")
    fun notificationsByDate(): DataSource.Factory<Int, Notification>
}