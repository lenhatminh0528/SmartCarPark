package com.example.smartparkinglot.di

import com.example.smartparkinglot.MyApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, ActivityBuilderModule::class])
interface AppComponent : AndroidInjector<DaggerApplication> {

    fun inject(myApplication: MyApplication)        //Field injection

    @Component.Builder
    interface Builder {
        fun build() : AppComponent

        @BindsInstance
        fun application(myApplication: MyApplication) : Builder
    }
}