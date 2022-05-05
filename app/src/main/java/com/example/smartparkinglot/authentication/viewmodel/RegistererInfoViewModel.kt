package com.example.smartparkinglot.authentication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartparkinglot.model.User
import com.example.smartparkinglot.retrofit.RESTClient
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.lang.Exception
import com.example.smartparkinglot.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegistererInfoViewModel : ViewModel() {
    private val TAG = "RegistererInfoViewModel"
    var username: MutableLiveData<String> = MutableLiveData()
    var password: MutableLiveData<String> = MutableLiveData()
    var cardId: MutableLiveData<String> = MutableLiveData()
    var address: MutableLiveData<String> = MutableLiveData()
    var carNumber: MutableLiveData<String> = MutableLiveData()
    var carNumberImg: MutableLiveData<String> = MutableLiveData()

    suspend fun callAPI() : Result<User> {
        return try {
            val jsonObject = JSONObject().apply {
                put("username", username.value)
                put("password", password.value)
                put("address", address.value)
                put("idcard", cardId.value)
                put("carnum", carNumber.value)
            }

            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()
            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
            val result = RESTClient.getApi().signUp(requestBody)
            withContext(Dispatchers.Main){
                if (result.isSuccessful) {
                    val gson = GsonBuilder().setPrettyPrinting().create()

                    val body = JsonParser.parseString(result.body()?.string())
                    val prettyDataFormat = gson.fromJson(body, JsonObject::class.java)

                    val status = prettyDataFormat.get("status").toString()
                    val msg = prettyDataFormat.get("msg").toString()

                    Log.d(TAG, "callAPI: status: $status")
                    if(status.contains("failure")) {
                        Result.Error(Exception("Something went wrong!"))
                    } else {
                        Result.Success(null,msg)
                    }
                } else {
                    Result.Error(Exception("Something went wrong!"))
                }
            }
        }catch (exception : Exception) {
            withContext(Dispatchers.Main){
                Result.Error(exception)
            }
        }
    }

}