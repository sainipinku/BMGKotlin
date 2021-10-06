package com.bookmygame.ui.user.model

import org.joda.time.DateTime

data class Slot (
    var id: String,
    var from_date: DateTime,
    var to_date: DateTime,
    var ground_id: String,
    var price: String,
    var no_of_overs: String,
    var ball_type: String,
    var status: String
)

data class Ground (
    var id: String,
    var user_id: String,
    var ground_name: String,
    var ground_address: String,
    var longitude: String,
    var latitude: String,
    var pitch: String,
    var status: String,
    var from_date: DateTime,
    var to_date: DateTime,
    var ground_id: String,
    var price: String,
    var no_of_overs: String,
    var ball_type: String,
    var slot: List<Slot>
 )


