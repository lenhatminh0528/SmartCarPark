package com.example.smartparkinglot.dashboard.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartparkinglot.model.User
import com.example.smartparkinglot.network.APIService
import com.example.smartparkinglot.network.RESTClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class UserInfoViewModel: ViewModel() {
    var user = MutableLiveData<User?>()

    fun fetchData(userId: String){

        viewModelScope.launch(Dispatchers.IO) {
            //JSON using JSONObject
            val jsonObject = JSONObject()
            with(jsonObject){
                put("col", "user")
                put("id", userId)
            }

            val response = RESTClient.createClient()
                .create(APIService::class.java)
                .showDB("user", userId.substring(1, userId.length - 1))
        }
    }
}