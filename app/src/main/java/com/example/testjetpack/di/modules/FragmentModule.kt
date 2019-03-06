package com.example.testjetpack.di.modules

import com.example.testjetpack.ui.main.myprofile.MyProfileFragment
import com.example.testjetpack.ui.main.notifications.NotificationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    internal abstract fun contributeNotificationFragment(): NotificationFragment

    @ContributesAndroidInjector
    internal abstract fun contributeMyProfileFragment(): MyProfileFragment
}