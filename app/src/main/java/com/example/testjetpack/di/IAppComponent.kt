package com.example.testjetpack.di

import com.example.testjetpack.MainApplication
import com.example.testjetpack.di.modules.ActivityModule
import com.example.testjetpack.di.modules.AppModule
import com.example.testjetpack.di.modules.FragmentModule
import com.example.testjetpack.di.modules.NetworkModule
import com.example.testjetpack.ui.main.MainActivityVM
import com.example.testjetpack.ui.main.myprofile.MyProfileFragmentVM
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, ActivityModule::class, FragmentModule::class,
        AppModule::class, NetworkModule::class]
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

        fun build(): IAppComponent
    }


    fun inject(mainActivityVM: MainActivityVM)
    fun inject(myProfileFragmentVM: MyProfileFragmentVM)
}