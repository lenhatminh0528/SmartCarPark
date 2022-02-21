package com.example.smartparkinglot.repository

import com.example.smartparkinglot.retrofit.APIService
import com.example.smartparkinglot.room.UserRoomDatabase

class CarParkRepository(var userRoomDatabase:UserRoomDatabase,var apiService: APIService) {


//     suspend fun getUserInfo(userId: String) : Result<UserEntity> {
//        return try {
//            val response = apiService.showDB("user",userId)
//        }catch (exception : Exception){
//            Result.Error(exception)
//        }
//    }
}