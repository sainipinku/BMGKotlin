package com.bookmygame.ui.groundOwner

data class Slot(
    var id: String,
    var noOfOvers: String,
    var fromDate: String,
    var toDate: String,
    var startTime: String,
    var endTime: String,
    var price: String,
    var ballType: String,
    var team1: Team? = null,
    var team2: Team? = null
) {
    data class Team(
        var id: String?,
        var name: String?
    )
}
