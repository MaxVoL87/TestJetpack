package com.example.testjetpack.di.modules

import com.example.testjetpack.ui.main.gitreposearch.GitRepoSearchFragment
import com.example.testjetpack.ui.main.gps.GpsFragment
import com.example.testjetpack.ui.main.myprofile.MyProfileFragment
import com.example.testjetpack.ui.main.notifications.NotificationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {


    @ContributesAndroidInjector
    internal abstract fun contributeGitRepoSearchFragment(): GitRepoSearchFragment

    @ContributesAndroidInjector
    internal abstract fun contributeMyProfileFragment(): MyProfileFragment

    @ContributesAndroidInjector
    internal abstract fun contributeGpsFragment(): GpsFragment

    @ContributesAndroidInjector
    internal abstract fun contributeNotificationFragment(): NotificationFragment
}