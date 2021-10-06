package com.bookmygame.network.model

import com.google.gson.annotations.SerializedName

data class UserGroundListResponse(
    @SerializedName("msg")
    val message: String,
    @SerializedName("result")
    val grounds: List<Ground>
) {
    data class Ground(
        @SerializedName("id")
        val Id: String,
        @SerializedName("user_id")
        val userId: String,
        @SerializedName("ground_name")
        var groundName: String,
        @SerializedName("ground_address")
        var groundAddress: String,
        @SerializedName("longitude")
        val longitude: String,
        @SerializedName("latitude")
        val latitude: String,
        @SerializedName("pitch")
        var pitchType: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("from_date")
        var fromData: String,
        @SerializedName("to_date")
        var toData: String,
        @SerializedName("ground_id")
        val groundId: String,
        @SerializedName("price")
        val price: String,
        @SerializedName("no_of_overs")
        var noOfOvers: String,
        @SerializedName("ball_type")
        val ballType: String,
        @SerializedName("slot")
        val slots: List<Slot>,
    ) {
        data class Slot(
            @SerializedName("id")
            val slotId: String,
            @SerializedName("from_date")
            val fromDate: String,
            @SerializedName("to_date")
            val toDate: String,
            @SerializedName("ground_id")
            val groundId: String,
            @SerializedName("price")
            val price: String,
            @SerializedName("no_of_overs")
            val noOfOvers: String,
            @SerializedName("ball_type")
            val ballType: String,
            @SerializedName("status") // Available, Partially Booked and Booked
            val status: String,
        )


    }
}
