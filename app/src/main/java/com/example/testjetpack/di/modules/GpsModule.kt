package com.example.testjetpack.di.modules

import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.gps.GpsLocationProvider
import com.example.testjetpack.dataflow.gps.LocationProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GpsModule {

    @Singleton
    @Provides
    fun provideLocationProvider(mainApplication: MainApplication): LocationProvider {
        return LocationProvider(mainApplication)
    }

    @Singleton
    @Provides
    fun provideGpsLocationProvider(mainApplication: MainApplication): GpsLocationProvider {
        return GpsLocationProvider(mainApplication)
    }
}