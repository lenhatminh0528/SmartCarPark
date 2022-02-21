package com.example.smartparkinglot.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smartparkinglot.room.dao.UserDao

abstract class UserRoomDatabase : RoomDatabase()  {
    abstract fun userDao() : UserDao

    companion object {
        private var INSTANCE : UserRoomDatabase? = null

        fun getInstance(context: Context): UserRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context,UserRoomDatabase::class.java, "user_database").build()
                INSTANCE = instance
                instance
            }
        }
    }
}