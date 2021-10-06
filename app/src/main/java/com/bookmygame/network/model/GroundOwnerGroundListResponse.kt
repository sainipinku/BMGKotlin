package com.bookmygame.network.model

import com.google.gson.annotations.SerializedName

data class GroundOwnerGroundListResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("msg")
    val message: String,
    @SerializedName("data")
    val grounds: List<Ground>
) {
    data class Ground(
        @SerializedName("id")
        val groundId: String,
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
        @SerializedName("slot")
        val slots: List<Slot>,
        @SerializedName("ground_images")
        val images: List<Image>
    ) {
        data class Slot(
            @SerializedName("id")
            val slotId: String,
            @SerializedName("ground_id")
            val groundId: String,
            @SerializedName("from_date")
            val fromDate: String,
            @SerializedName("to_date")
            val toDate: String,
            @SerializedName("from_time")
            val fromTime: String,
            @SerializedName("to_time")
            val toTime: String,
            @SerializedName("price")
            val price: String,
            @SerializedName("no_of_overs")
            val noOfOvers: String,
            @SerializedName("ball_type")
            val ballType: String,
            @SerializedName("status") // Available, Partially Booked and Booked
            val status: String,
            @SerializedName("booking")
            val bookings: List<Team>
        ) {
            data class Team(
                @SerializedName("team_name")
                val teamName: String
            )
        }

        data class Image(
            @SerializedName("id")
            val imageId: String,
            @SerializedName("ground_id")
            val groundId: String,
            @SerializedName("image")
            val imageUrl: String
        )
    }
}
