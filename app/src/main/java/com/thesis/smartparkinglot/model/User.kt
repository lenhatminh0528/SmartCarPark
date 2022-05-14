package com.thesis.smartparkinglot.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id")
    var id: String,
    @SerializedName("username")
    var username: String,
    @SerializedName("address")
    var address: String,
    @SerializedName("idcard")
    var cardId: String,
    @SerializedName("carnum")
    var carNumber: String,
    @SerializedName("link_qr")
    var linkQr: String,
)