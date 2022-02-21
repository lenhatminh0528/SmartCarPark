package com.example.smartparkinglot.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smartparkinglot.room.entities.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table ORDER BY username DESC")
    fun getListUser() : List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: UserEntity)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()
}