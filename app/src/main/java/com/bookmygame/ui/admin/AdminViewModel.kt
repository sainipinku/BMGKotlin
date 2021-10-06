package com.bookmygame.ui.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bookmygame.network.ApiClient
import com.bookmygame.network.model.AddGroundResponse
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminViewModel : ViewModel() {

    fun addGround(
        userId: String,
        mobileNumber: String,
        groundName: String,
        groundAddress: String,
        groundLocation: LatLng
    ) {
        val addGroundResponse =
            ApiClient.apiService.addGround(
                userId,
                mobileNumber,
                groundName,
                groundAddress,
                groundLocation.latitude.toString(),
                groundLocation.longitude.toString()
            )
        addGroundResponse.enqueue(object : Callback<AddGroundResponse> {
            override fun onResponse(
                call: Call<AddGroundResponse>,
                response: Response<AddGroundResponse>
            ) {
                Log.d("Basava", "addGroundResponse onResponse isSuccessful -> ${response.isSuccessful}")
                Log.d("Basava", "addGroundResponse onResponse body -> ${response.body()}")
                Log.d("Basava", "addGroundResponse onResponse message -> ${response.message()}")
            }

            override fun onFailure(call: Call<AddGroundResponse>, t: Throwable) {
                Log.d("Basava", "addGroundResponse onFailure message -> ${t.message}")
            }
        })
    }
}