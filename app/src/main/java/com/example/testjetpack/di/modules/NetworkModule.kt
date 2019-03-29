package com.example.testjetpack.di.modules

import com.example.testjetpack.MainApplicationContract
import com.example.testjetpack.dataflow.network.IDataApi
import com.example.testjetpack.dataflow.network.IGitApi
import com.example.testjetpack.di.annotations.BaseRetrofit
import com.example.testjetpack.di.annotations.GitRetrofit
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideGitApiInterface(@GitRetrofit retrofit: Retrofit): IGitApi = retrofit.create(IGitApi::class.java)

    @Singleton
    @Provides
    fun provideBaseApiInterface(@BaseRetrofit retrofit: Retrofit): IDataApi = retrofit.create(IDataApi::class.java)

    @Singleton
    @Provides
    @GitRetrofit
    fun provideGitRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MainApplicationContract.API_GIT_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    @BaseRetrofit
    fun provideBaseRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MainApplicationContract.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(loggerInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val original = it.request()
            val request = original.newBuilder()
                .method(original.method(), original.body())
                .build()

            it.proceed(request)
        }
        .addNetworkInterceptor(loggerInterceptor)
        .connectTimeout(40, TimeUnit.SECONDS)
        .readTimeout(40, TimeUnit.SECONDS)
        .writeTimeout(40, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideHttpLoggerInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Timber.tag(NETWORK_TAG).d(it)
        })
        logger.level = HttpLoggingInterceptor.Level.BODY

        return logger
    }

    companion object {
        private const val NETWORK_TAG = "Network"
    }

}