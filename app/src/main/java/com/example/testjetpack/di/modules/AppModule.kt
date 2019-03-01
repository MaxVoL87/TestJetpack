package com.example.testjetpack.di.modules

import com.example.testjetpack.BuildConfig
import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.repository.DataRepository
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideDataRepository(): IDataRepository {
        return DataRepository()
    }

    @Provides
    @Singleton
    fun providePicasso(application: MainApplication): Picasso {
        val client = OkHttpClient.Builder()
        //.addInterceptor(ServiceGenerator.createOAuth2Interceptor())
        if (BuildConfig.DEBUG) {
            client.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        val okHttp3Downloader = OkHttp3Downloader(client.build())
        return Picasso.Builder(application)
            .downloader(okHttp3Downloader)
            .loggingEnabled(BuildConfig.DEBUG)
            .build()
    }
}