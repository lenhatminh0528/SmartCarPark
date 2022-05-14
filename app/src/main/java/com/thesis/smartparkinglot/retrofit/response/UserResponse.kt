package com.thesis.smartparkinglot.retrofit.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserResponse (
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
) : Parcelable