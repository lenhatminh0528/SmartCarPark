package com.example.smartparkinglot.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartparkinglot.authentication.viewmodel.LoginViewModel
import com.example.smartparkinglot.authentication.viewmodel.RegistererInfoViewModel
import com.example.smartparkinglot.dashboard.viewmodel.UserInfoViewModel
import com.example.smartparkinglot.dashboard.viewmodel.UserInfoViewModel_Factory
import com.example.smartparkinglot.dashboard.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelBindingModule {
//
    @Binds
//    @IntoMap
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
//    @IntoMap
    abstract fun bindUserInfoViewModel(userInfoViewModel: UserInfoViewModel): UserInfoViewModel

    @Binds
//    @IntoMap
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel) : LoginViewModel

    @Binds
//    @IntoMap
    abstract fun bindRegisterInfoViewModel(registererInfoViewModel: RegistererInfoViewModel) : RegistererInfoViewModel

}