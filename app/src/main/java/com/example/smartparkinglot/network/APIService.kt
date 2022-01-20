package com.example.smartparkinglot.network

import com.example.smartparkinglot.model.User
import okhttp3.RequestBody
//import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


private const val BASE_URL = "https://d2ac-203-205-33-94.ngrok.io"
//private const val BASE_URL = "http://dummy.restapiexample.com"
interface APIService {
//    @POST("/signup")
//    suspend fun signupUser(@Body requestBody: RequestBody): Response<Respon>
//    @POST("/api/v1/create")
    @POST("/signup")
    suspend fun signUp(@Body requestBody: RequestBody): Response<ResponseBody>
    @POST("/signIn")
    suspend fun signIn(@Body requestBody: RequestBody): Response<ResponseBody>

}
