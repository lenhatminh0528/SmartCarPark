package com.example.smartparkinglot.network

import com.example.smartparkinglot.model.User
import okhttp3.RequestBody
//import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


//private const val BASE_URL = "https://d2ac-203-205-33-94.ngrok.io"
//private const val BASE_URL = "http://dummy.restapiexample.com"
interface APIService {

    @POST("/signup")
    suspend fun signUp(@Body requestBody: RequestBody): Response<ResponseBody>
    @POST("/signin")
    suspend fun signIn(@Body requestBody: RequestBody): Response<ResponseBody>
    @POST("/fetchMe")
    suspend fun fetchMe(@Body requestBody: RequestBody): Response<ResponseBody>
    @GET("/showdb")
    suspend fun showDB(@Query("col") col: String, @Query("id") userId: String): Response<ResponseBody>
//    suspend fun showDB(@QueryMap Map<String, String> paramsData): Response<ResponseBody>
}
