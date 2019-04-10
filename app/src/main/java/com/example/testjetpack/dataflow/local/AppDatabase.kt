package com.example.testjetpack.dataflow.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testjetpack.models.git.db.GitRepositoryView
import com.example.testjetpack.models.git.db.GitRepository
import com.example.testjetpack.models.git.License
import com.example.testjetpack.models.own.Notification
import com.example.testjetpack.models.git.User
import com.example.testjetpack.models.gps.Location

// https://www.youtube.com/watch?v=sU-ot_Oz3AE&feature=youtu.be
@Database(
    version = 1,
    entities = [GitRepository::class, User::class, License::class, Notification::class, Location::class],
    views = [GitRepositoryView::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getGitRepositoryDao(): IGitRepositoryDao

    abstract fun getGitLicenseDao(): IGitLicenseDao

    abstract fun getGitUserDao(): IGitUserDao

    abstract fun getGitDao(): IGitDao


    abstract fun getNotificationDao(): INotificationDao


    abstract fun getLocationDao(): ILocationDao


    fun clearAllGitData() {
        getGitRepositoryDao().clearAll()
        getGitLicenseDao().clearAll()
        getGitUserDao().clearAll()
    }
}