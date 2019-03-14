package com.example.testjetpack

import com.example.testjetpack.di.DaggerIAppComponent
import com.example.testjetpack.di.IAppComponent
import com.example.testjetpack.di.modules.AppModule
import com.example.testjetpack.di.modules.DatabaseModule
import com.example.testjetpack.di.modules.NetworkModule
import dagger.android.support.DaggerApplication
import timber.log.Timber

class MainApplication : DaggerApplication() {

    private val applicationInjector = DaggerIAppComponent.builder()
        .application(this)
        .appModule(AppModule())
        .netModule(NetworkModule())
        .dbModule(DatabaseModule())
        .build()

    override fun applicationInjector() = applicationInjector

    companion object {
        @JvmStatic
        lateinit var component: IAppComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = applicationInjector

        applicationInjector.inject(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return super.createStackElementTag(element) + "::Line:" + element.lineNumber + "::" + element.methodName + "()"
                }
            })
        }

    }
}