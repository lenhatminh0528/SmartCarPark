package com.thesis.smartparkinglot.authentication.viewmodel

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.*
import com.thesis.smartparkinglot.Result
import com.thesis.smartparkinglot.retrofit.RESTClient
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.thesis.smartparkinglot.AppShareRefs
import com.thesis.smartparkinglot.dashboard.DashboardActivity
import com.thesis.smartparkinglot.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

enum class StateType {
    NONE,
    SIGN_IN,
    LOADING,
    SIGN_UP,
}

sealed class StateView<T>(
    val type: StateType,
    val data: T? = null,
    val message: String? = null,
) {
    class None<T>(type: StateType): StateView<T>(type)
    class Loading<T>(type: StateType): StateView<T>(type)
    class Success<T>(type: StateType, data: T, message: String): StateView<T>(type, data, message)
    class Error<T>(type: StateType,data:T? = null ,message: String): StateView<T>(type, data, message)
}

class LoginViewModel : ViewModel() {
    var userName = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var apiState = MutableLiveData<StateView<Any>>().apply {
        StateView.None<Any>(StateType.NONE)
    }


    var isEnableSignInBtn: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        fun update() {
            val userName = userName.value ?: return
            val password = password.value ?: return
            value = userName.isNotEmpty() && password.isNotEmpty()
            Log.d("SmartParkingLot", "update: value: $value")
        }

        addSource(userName){ update()}
        addSource(password){ update()}
    }

    private suspend fun callSignInAPI() : Result<String> {
        return try {
            val jsonObject = JSONObject()
            with(jsonObject){
                put("username", userName.value)
                put("password", password.value)
            }

            val response = RESTClient.getApi()
                .signIn(jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull()))

//            val gson = GsonBuilder().create()
//                val dataFormat = gson.fromJson(JsonParser.parseString(response.body()?.string()), JsonObject::class.java)

            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                    val s = response.body()!!.status
                    if (s.contains("success")) {
                        Result.Success(response.body()!!.userId, "Login Successful!")
                    } else {
                        Result.Error(Exception("Not found user!"))
                    }
                } else {
                    Result.Error(Exception("Something went wrong!"))
                }
            }
        }catch (exception: Exception){
            Result.Error(exception)
        }
    }

    fun clickSignIn(view: View) {
        apiState.postValue(StateView.Loading(StateType.LOADING))
        if(!NetworkUtils.isNetworkConnect(view.context)) {
//            view.context.showErrorDialog("No network connection!")
            apiState.postValue(StateView.Error(StateType.SIGN_IN, null, "Network Connection error!"))
        } else {
            viewModelScope.launch {
                when(val result = callSignInAPI()) {
                    is Result.Success -> {
                        result.data?.let {
                            Log.d("SmartParkingLot", "result success: $it")
                            apiState.postValue(StateView.Success(StateType.SIGN_IN, data = it, ""))

                        }
                    }
                    is Result.Error ->
                        result.exception.message?.let {
                            Log.d("SmartParkingLot", "Result error: $it")
                            apiState.postValue(StateView.Error(StateType.SIGN_IN, null, it))
                        }
                }
            }
        }
    }
}