package com.example.testjetpack.di.modules

import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.gps.GpsLocationProvider
import com.example.testjetpack.dataflow.gps.GmsLocationProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GpsModule {

    @Singleton
    @Provides
    fun provideLocationProvider(mainApplication: MainApplication): GmsLocationProvider {
        return GmsLocationProvider(mainApplication)
    }

    @Singleton
    @Provides
    fun provideGpsLocationProvider(mainApplication: MainApplication): GpsLocationProvider {
        return GpsLocationProvider(mainApplication)
    }
}