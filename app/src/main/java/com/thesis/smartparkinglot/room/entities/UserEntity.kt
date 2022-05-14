package com.thesis.smartparkinglot.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

@Entity(tableName = "user_table")
data class UserEntity (

    @PrimaryKey
    @ColumnInfo(name = "id")
    @field:Json(name = "id")
    var id : String,
    @field:Json(name = "username")
    @ColumnInfo(name = "username")
    var userName : String,
    @ColumnInfo(name ="address")
    @field:Json(name = "address")
    var address: String,
    @ColumnInfo(name ="idcard")
    @field:Json(name = "cardId")
    var cardId: String,
    @ColumnInfo(name ="carNumber")
    @field:Json(name = "carNumber")
    var carNumber: String,
    @ColumnInfo(name = "link_qr")
    @field:Json(name = "linkQr")
    var linkQr: String,

)