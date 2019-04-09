package com.example.testjetpack.di

import com.example.testjetpack.MainApplicationContract
import com.example.testjetpack.dataflow.network.IDataApi
import com.example.testjetpack.dataflow.network.IGitApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

val networkModule = module {

    single<IGitApi> { get<Retrofit>(named(gitRetrofitName)).create(IGitApi::class.java) }

    single<IDataApi> { get<Retrofit>(named(baseRetrofitName)).create(IDataApi::class.java) }


    single<Retrofit>(named(gitRetrofitName)) {
        Retrofit.Builder()
            .baseUrl(MainApplicationContract.API_GIT_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    single<Retrofit>(named(baseRetrofitName)) {
        Retrofit.Builder()
            .baseUrl(MainApplicationContract.API_BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                val request = original.newBuilder()
                    .method(original.method(), original.body())
                    .build()

                it.proceed(request)
            }
            .addNetworkInterceptor(get())
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .build()
    }

    single<Interceptor> {
        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Timber.tag("Network").d(it)
        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

}