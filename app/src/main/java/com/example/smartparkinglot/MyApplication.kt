package com.example.smartparkinglot

import android.app.Application
import com.example.smartparkinglot.di.AppComponent
import com.example.smartparkinglot.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerContentProvider
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MyApplication: Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private lateinit var appComponent : AppComponent

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    private fun initDagger() {
        appComponent = DaggerAppComponent
            .builder()
            .application(this)
            .build()
        appComponent.inject(this)
    }
}