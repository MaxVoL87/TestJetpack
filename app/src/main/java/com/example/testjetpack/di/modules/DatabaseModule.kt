package com.example.testjetpack.di.modules

import androidx.room.Room
import com.example.testjetpack.BuildConfig
import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.local.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: MainApplication): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            databaseName
        ).fallbackToDestructiveMigration()
            .build()
    }

    companion object {
        private const val databaseName = BuildConfig.DB_NAME
    }
}