package com.thesis.smartparkinglot.retrofit.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class SignInResponse (
    @SerializedName("status")
    var status: String,
    @SerializedName("id_user")
    var userId: String,
): Parcelable
