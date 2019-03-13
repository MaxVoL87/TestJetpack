package com.example.testjetpack.dataflow.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testjetpack.models.git.GitRepository
import com.example.testjetpack.models.git.License
import com.example.testjetpack.models.Notification
import com.example.testjetpack.models.git.User

@Database(
    version = 1,
    entities = [GitRepository::class, User::class, License::class, Notification::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gitRepositoryDao(): IGitRepositoryDao

    abstract fun notificationDao(): INotificationDao
}