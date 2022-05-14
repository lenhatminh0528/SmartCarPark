package com.thesis.smartparkinglot.authentication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thesis.smartparkinglot.Result
import com.thesis.smartparkinglot.retrofit.RESTClient
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class LoginViewModel : ViewModel() {
    var userName = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    suspend fun callAPI() : Result<String> {
        return try {
            val jsonObject = JSONObject()
            with(jsonObject){
                put("username", userName.value)
                put("password", password.value)
            }

            val response = RESTClient.getApi()
                .signIn(jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull()))
            val gson = GsonBuilder().create()
                val dataFormat = gson.fromJson(JsonParser.parseString(response.body()?.string()), JsonObject::class.java)

            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                    val status = dataFormat.get("status").toString()
                    if (status.contains("success")) {
                        //login successful
                        val id = dataFormat.get("id_user").toString()
                        Result.Success(id, "Login Successful!")
                    } else {
                        Result.Error(Exception("Something went wrong!"))
                    }
                } else {
                    Result.Error(Exception("Something went wrong!"))
                }
            }
        }catch (exception: Exception){
            Result.Error(exception)
        }
    }
}