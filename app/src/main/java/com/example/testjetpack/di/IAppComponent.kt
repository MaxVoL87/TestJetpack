package com.example.testjetpack.di

import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.SearchGitReposPListBoundaryCallback
import com.example.testjetpack.di.modules.*
import com.example.testjetpack.tasks.workers.NotificationDownloadWorker
import com.example.testjetpack.ui.main.MainActivityVM
import com.example.testjetpack.ui.main.gitreposearch.GitRepoSearchFragmentVM
import com.example.testjetpack.ui.main.myprofile.MyProfileFragmentVM
import com.example.testjetpack.ui.main.notifications.NotificationFragmentVM
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, ActivityModule::class, FragmentModule::class,
        AppModule::class, NetworkModule::class, DatabaseModule::class]
)
interface IAppComponent : AndroidInjector<MainApplication> {
    override fun inject(application: MainApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MainApplication): Builder

        @BindsInstance
        fun appModule(appModule: AppModule): Builder

        @BindsInstance
        fun netModule(netModule: NetworkModule): Builder

        @BindsInstance
        fun dbModule(dbModule: DatabaseModule): Builder

        fun build(): IAppComponent
    }

    // ui
    fun inject(mainActivityVM: MainActivityVM)
    fun inject(gitRepoSearchFragmentVM: GitRepoSearchFragmentVM)
    fun inject(notificationFragmentVM: NotificationFragmentVM)
    fun inject(myProfileFragmentVM: MyProfileFragmentVM)

    fun inject(searchGitReposPListBoundaryCallback: SearchGitReposPListBoundaryCallback)

    // tasks
    fun inject(notificationDownloadWorker: NotificationDownloadWorker)
}