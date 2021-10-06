package com.bookmygame.network.model

import com.google.gson.annotations.SerializedName

data class LoginTypeResponse(
    @SerializedName("error")
    val error: Boolean,

    @SerializedName("msg")
    val message: String,

    @SerializedName("token_code")
    val tokenCode: String?
)
