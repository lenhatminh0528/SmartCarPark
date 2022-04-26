package com.example.smartparkinglot.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartparkinglot.Result
import com.example.smartparkinglot.model.User
import com.example.smartparkinglot.repository.CarParkRepository
import com.example.smartparkinglot.retrofit.RESTClient
import com.example.smartparkinglot.retrofit.response.UserResponse
import com.example.smartparkinglot.room.entities.UserEntity
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import java.security.Provider
import javax.inject.Inject

class ViewModelFactory @Inject constructor(var repository: CarParkRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserInfoViewModel::class.java)) {
            return UserInfoViewModel(repository) as T
        }
        throw IllegalAccessException("Un know view model")
    }

}

class UserInfoViewModel @Inject constructor(var repository: CarParkRepository): ViewModel() {
    var user = MutableLiveData<UserResponse?>()
    var errorMessage = MutableLiveData<String?>()

    suspend fun fetchUserInfo(userId: String) {

        when (val result = repository.getUserInfo(userId)) {
            is Result.Success -> {
                user.postValue(result.data)
                errorMessage.postValue(null)
            }
            is Result.Error -> {
                errorMessage.postValue(result.exception.message)
            }
        }
    }

}