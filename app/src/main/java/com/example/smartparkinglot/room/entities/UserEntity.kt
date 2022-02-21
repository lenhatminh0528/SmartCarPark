package com.example.smartparkinglot.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_table")
data class UserEntity (

    @ColumnInfo(name = "id") var id : String,
    @ColumnInfo(name = "username") var userName : String,
    @ColumnInfo(name ="address") var address: String,
    @ColumnInfo(name ="idcard") var cardId: String,
    @ColumnInfo(name ="carnum") var carNumber: String,
    @ColumnInfo(name = "link_qr") var linkQr: String,

)