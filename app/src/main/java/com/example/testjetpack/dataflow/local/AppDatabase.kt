package com.example.testjetpack.dataflow.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testjetpack.models.GitRepositoryView
import com.example.testjetpack.models.git.db.GitRepository
import com.example.testjetpack.models.git.License
import com.example.testjetpack.models.own.Notification
import com.example.testjetpack.models.git.User

@Database(
    version = 1,
    entities = [GitRepository::class, User::class, License::class, Notification::class],
    views = [GitRepositoryView::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getGitRepositoryDao(): IGitRepositoryDao

    abstract fun getGitLicenseDao(): IGitLicenseDao

    abstract fun getGitUserDao(): IGitUserDao

    abstract fun getGitDao(): IGitDao


    abstract fun notificationDao(): INotificationDao

    fun clearAllGitData() {
        getGitRepositoryDao().clearAll()
        getGitLicenseDao().clearAll()
        getGitUserDao().clearAll()
    }
}