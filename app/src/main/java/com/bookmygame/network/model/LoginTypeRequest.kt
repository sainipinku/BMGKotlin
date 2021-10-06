package com.bookmygame.network.model

import com.google.gson.annotations.SerializedName

data class LoginTypeRequest(
    @SerializedName("mobile_no")
    val mobileNumber: String,

    @SerializedName("login_type")
    val loginType: String
)
