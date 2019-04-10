package com.example.testjetpack.di

import com.example.testjetpack.dataflow.gps.GmsLocationProvider
import com.example.testjetpack.dataflow.gps.GpsLocationProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val gpsModule = module {

    factory { GmsLocationProvider(androidContext()) }

    factory { GpsLocationProvider(androidContext()) }
}