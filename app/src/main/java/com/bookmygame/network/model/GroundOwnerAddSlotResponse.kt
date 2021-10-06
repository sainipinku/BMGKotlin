package com.bookmygame.network.model

import com.google.gson.annotations.SerializedName

data class GroundOwnerAddSlotResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("msg")
    val message: String,
)