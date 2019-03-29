package com.example.testjetpack.di.modules

import androidx.room.Room
import com.example.testjetpack.MainApplication
import com.example.testjetpack.MainApplicationContract.DATABASE_NAME
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
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

}