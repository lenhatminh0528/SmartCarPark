package com.thesis.smartparkinglot.retrofit

import com.thesis.smartparkinglot.retrofit.response.UserResponse
import okhttp3.RequestBody
//import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @POST("/signup")
    suspend fun signUp(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("/signin")
    suspend fun signIn(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("/fetchMe")
    suspend fun fetchMe(@Body requestBody: RequestBody): Response<ResponseBody>

    @GET("/showdb")
    suspend fun showDB(@Query("col") col: String, @Query("id") userId: String): Response<UserResponse>
}
