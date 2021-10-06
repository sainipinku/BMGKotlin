package com.bookmygame.network.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("msg")
    val message: String,
    @SerializedName("data")
    val data: List<User>
) {
    data class User(
        @SerializedName("user_id")
        val userId: String,
        @SerializedName("name")
        val userName: String,
        @SerializedName("mobile_number")
        val mobileNumber: String,
        @SerializedName("last_login")
        val lastLogin: String,
        @SerializedName("created")
        val created: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("role_name")
        val roleName: String,
    )
}
