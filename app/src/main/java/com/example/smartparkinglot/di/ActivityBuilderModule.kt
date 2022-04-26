package com.example.smartparkinglot.di

import com.example.smartparkinglot.authentication.AuthActivity
import com.example.smartparkinglot.authentication.fragment.LoginFragment
import com.example.smartparkinglot.authentication.fragment.RegisterInfoFragment
import com.example.smartparkinglot.dashboard.DashboardActivity
import com.example.smartparkinglot.dashboard.QrCodeFragment
import com.example.smartparkinglot.dashboard.UserInfoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ViewModelBindingModule::class])
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun dashboardActivity(): DashboardActivity

    @ContributesAndroidInjector
    abstract fun authActivity(): AuthActivity

    @ContributesAndroidInjector
    abstract fun loginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun RegisterInfoFragment(): RegisterInfoFragment

    @ContributesAndroidInjector
    abstract fun qrCodeFragment(): QrCodeFragment

    @ContributesAndroidInjector
    abstract fun userInfoFragment(): UserInfoFragment
}