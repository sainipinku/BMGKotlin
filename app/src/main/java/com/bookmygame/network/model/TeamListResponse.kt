package com.bookmygame.network.model

import com.google.gson.annotations.SerializedName

data class TeamListResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("msg")
    val message: String,
    @SerializedName("data")
    val data: List<Team>
) {
    data class Team(
        @SerializedName("id")
        val teamId: String,
        @SerializedName("name")
        val teamName: String
    )
}
