package com.example.testjetpack.di

import android.content.res.Resources
import android.os.Build
import androidx.collection.LruCache
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequest
import com.example.testjetpack.BuildConfig
import com.example.testjetpack.dataflow.repository.DataRepository
import com.example.testjetpack.dataflow.repository.GitDataRepository
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.dataflow.repository.IGitDataRepository
import com.example.testjetpack.ui.dialog.message.MessageDialogFragmentVM
import com.example.testjetpack.ui.dialog.progress.ProgressDialogFragmentVM
import com.example.testjetpack.workers.NotificationDownloadWorker
import com.example.testjetpack.ui.main.MainActivityVM
import com.example.testjetpack.ui.main.gitrepodetails.GitRepoDetailsFragmentVM
import com.example.testjetpack.ui.main.gitreposearch.GitRepoSearchFragmentVM
import com.example.testjetpack.ui.main.gps.GpsFragmentVM
import com.example.testjetpack.ui.main.myprofile.MyProfileFragmentVM
import com.example.testjetpack.ui.main.mystatus.MyStatusFragmentVM
import com.example.testjetpack.ui.main.mytrip.MyTripFragmentVM
import com.example.testjetpack.ui.main.notifications.NotificationsFragmentVM
import com.example.testjetpack.ui.signin.SignInActivityVM
import com.example.testjetpack.ui.signin.passwordrecovery.PasswordRecoveryFragmentVM
import com.example.testjetpack.ui.signin.signin.SignInFragmentVM
import com.example.testjetpack.ui.signin.signup.SignUpFragmentVM
import com.example.testjetpack.ui.splash.SplashActivityVM
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

// https://insert-koin.io/docs/2.0/documentation/reference/index.html
val appModule = module {

    single<Resources> { androidContext().resources }

    single<Gson> {
        GsonBuilder()
            .serializeNulls()
            .create()
    }

    single<Picasso> {
        val builder = OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) addNetworkInterceptor(get())
        }
        val okHttp3Downloader = OkHttp3Downloader(builder.build())
        Picasso.Builder(androidContext())
            .downloader(okHttp3Downloader)
            .loggingEnabled(BuildConfig.DEBUG)
            .build()
    }

    single<LruCache<String, Any>> { LruCache(100)  }

    // single instance of IGitDataRepository
    single<IGitDataRepository> { GitDataRepository(get(), get()) }

    // single instance of IGitDataRepository
    single<IDataRepository> { DataRepository(get(), get()) }

    // Simple Presenter Factory
    factory(named<NotificationDownloadWorker>()) {
        val constraints = Constraints.Builder()
            .setRequiresDeviceIdle(false) // only when device Idle
            .setRequiresCharging(false) // only when device charging
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) setTriggerContentMaxDelay(
                    1,
                    TimeUnit.MINUTES
                )
            }
            .build()

        // 15 minutes is min interval
        PeriodicWorkRequest.Builder(NotificationDownloadWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
    }

//    // scope for MyScopeActivity
//    scope(named<MyScopeActivity>()) {
//        // scoped MyScopePresenter instance
//        scoped { MyScopePresenter(get()) }
//    }

    // MyViewModel ViewModel
    viewModel { ProgressDialogFragmentVM() }
    viewModel { MessageDialogFragmentVM() }

    viewModel { SplashActivityVM() }
    viewModel { SignInActivityVM() }
    viewModel { MainActivityVM(get()) }

    viewModel { SignInFragmentVM(get()) }
    viewModel { PasswordRecoveryFragmentVM(get()) }
    viewModel { SignUpFragmentVM(get()) }

    viewModel { GitRepoSearchFragmentVM(get()) }
    viewModel { GitRepoDetailsFragmentVM(get(), get(), get()) }
    viewModel { MyProfileFragmentVM(get(), get()) }
    viewModel { MyTripFragmentVM(get()) }
    viewModel { GpsFragmentVM(get(), get(), get()) }
    viewModel { MyStatusFragmentVM(get()) }
    viewModel { NotificationsFragmentVM(get()) }
}