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

class ViewModelFactory(var repository: CarParkRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserInfoViewModel::class.java)) {
            return UserInfoViewModel(repository) as T
        }
        throw IllegalAccessException("Unknow viewmodel")
    }

}

class UserInfoViewModel(var repository: CarParkRepository): ViewModel() {
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

//    suspend fun fetchData(userId: String): Result<User>{
            //JSON using JSONObject
//        return try {
//            val jsonObject = JSONObject()
//            with(jsonObject){
//                put("col", "user")
//                put("id", userId)
//            }
//
//            val response = RESTClient.getApi()
//                .showDB("user", userId.substring(1, userId.length - 1))
//            if(response.isSuccessful) {
//                val gson = GsonBuilder().setPrettyPrinting().create()
//
//                val prettyJson = gson.fromJson(
//                    JsonParser.parseString(
//                        response.body()
//                            ?.string()), JsonObject::class.java)
//
//                val status: String = prettyJson.get("status").toString()
//
//                withContext(Dispatchers.Main){
//                    if(status.contains("success")){
//                        val data = gson.fromJson(prettyJson.get("data"),User::class.java)
//                        user.value = data
//                        Result.Success(data, "Fetch user successful!")
//
//                    }else {
//                        Result.Error(Exception("Something went wrong!"))
//                    }
//                }
//            } else {
//                withContext(Dispatchers.Main){
//                    Result.Error(Exception("Something went wrong!"))
//                }
//            }
//        } catch (exception: Exception) {
//                withContext(Dispatchers.Main) {
//                    Result.Error(exception)
//                }
//        }
//    }

}