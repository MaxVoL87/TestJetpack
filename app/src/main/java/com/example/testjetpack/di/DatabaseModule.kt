package com.example.testjetpack.di

import androidx.room.Room
import com.example.testjetpack.MainApplicationContract
import com.example.testjetpack.dataflow.local.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, MainApplicationContract.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

}