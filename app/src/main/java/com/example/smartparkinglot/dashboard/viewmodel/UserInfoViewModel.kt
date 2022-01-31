package com.example.smartparkinglot.dashboard.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartparkinglot.model.User

class UserInfoViewModel: ViewModel() {
    var user = MutableLiveData<User?>()
}