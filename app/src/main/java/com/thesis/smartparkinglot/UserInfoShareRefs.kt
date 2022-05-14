package com.thesis.smartparkinglot

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.thesis.smartparkinglot.model.User
import com.google.gson.Gson

object AppShareRefs {
    private const val USER_INFO = "USER_INFO"
    private const val NAME = "NAME"
    private const val USER_ID = "USER_ID"
    private const val ADDRESS = "ADDRESS"
    private const val DOB = "DOB"
    private const val CAR_NUMBER = "CAR_NUMBER"
    private const val CAR_NUMBER_IMG = "CAR_NUMBER_IMG"

    private fun preferences(context: Context):SharedPreferences{
        return context.getSharedPreferences("user_shared_prefs", Context.MODE_PRIVATE)
    }

//    fun setInfo(context: Context,
//                name: String,
//                userId: String,
//                address: String,
//                dob:String,
//                carNumber: String,
//                carNumberImg: String ){
//        preferences(context).edit().apply(){
//            putString(NAME, name)
//            putString(USER_ID, userId)
//            putString(DOB, dob)
//            putString(ADDRESS, address)
//            putString(CAR_NUMBER, carNumber)
//            putString(CAR_NUMBER_IMG, carNumberImg)
//            apply()
//        }
//    }

    @SuppressLint("CommitPrefEdits")
    fun setInfo(context: Context,
                user: User){
        val gsonData = Gson().toJson(user)
        val prefsEditor: SharedPreferences.Editor = preferences(context).edit()
        prefsEditor.putString(USER_INFO, gsonData)
        prefsEditor.apply()
    }

    fun getUserId(context: Context): String? {
        return preferences(context).getString(USER_ID, "")
    }

    fun setUserId(context: Context, userId: String) {
        preferences(context).edit(){
            putString(USER_ID, userId)
            apply()
        }
    }

    fun getInfo (context: Context): User{
        val gsonData = Gson()
        val jsonPrefs = preferences(context).getString(USER_INFO, null)
        return gsonData.fromJson(jsonPrefs, User::class.java)
    }
}