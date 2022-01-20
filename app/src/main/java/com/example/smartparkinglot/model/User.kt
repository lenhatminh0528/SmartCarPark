package com.example.smartparkinglot.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("userName")
    var name: String,
    @SerializedName("address")
    var address: String,
    @SerializedName("cardId")
    var cardId: String,
    @SerializedName("carnum")
    var carNumber: String,
    var carNumberImg: String,
)