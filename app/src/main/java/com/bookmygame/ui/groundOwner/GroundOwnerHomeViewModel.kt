package com.bookmygame.ui.groundOwner

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bookmygame.network.ApiClient
import com.bookmygame.network.model.GroundOwnerGroundListResponse
import com.bookmygame.util.Event
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroundOwnerHomeViewModel : ViewModel() {

//    private val grounds: MutableLiveData<List<Ground>> by lazy {
//        MutableLiveData<List<Ground>>().also {
//            loadGrounds(it)
//        }
//    }
//
//    fun getGrounds(): LiveData<List<Ground>> {
//        return grounds
//    }
//
//    private fun loadGrounds(mutableLiveData: MutableLiveData<List<Ground>>) {
//        // Do an asynchronous operation to fetch users.
//        val groundsList = mutableListOf<Ground>()
//        for (i in 1..10) {
//            groundsList.add(Ground("Ground $i"))
//        }
//        mutableLiveData.value = groundsList
//    }

    private val mutableGroundOwnerGroundListResponse =
        MutableLiveData<Event<GroundOwnerGroundListResponse>>()
    val groundOwnerGroundListResponse: LiveData<Event<GroundOwnerGroundListResponse>> get() = mutableGroundOwnerGroundListResponse

    private val mutableErrorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> get() = mutableErrorMessage

    fun getGrounds(userId: String) {
        Log.d("Basava", "getGrounds: userId -> $userId")

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .build()

        val groundsResponse =
            ApiClient.apiService.groundOwnerGroundList(requestBody)
        groundsResponse.enqueue(object : Callback<GroundOwnerGroundListResponse> {
            override fun onResponse(
                call: Call<GroundOwnerGroundListResponse>,
                response: Response<GroundOwnerGroundListResponse>
            ) {
                Log.d(
                    "Basava",
                    "GroundOwnerGroundListResponse onResponse isSuccessful -> ${response.isSuccessful}"
                )
                Log.d(
                    "Basava",
                    "GroundOwnerGroundListResponse onResponse body -> ${response.body()}"
                )
                if (response.isSuccessful) {
                    mutableGroundOwnerGroundListResponse.value = Event(response.body())
                } else {
                    Log.e("Basava", "Something went wrong")
                    Log.e("Basava", "Something went wrong: message -> ${response.message()}")
                    mutableErrorMessage.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<GroundOwnerGroundListResponse>, t: Throwable) {
                Log.d("Basava", "GroundOwnerGroundListResponse onFailure message -> ${t.message}")
                mutableErrorMessage.value = Event(t.message ?: "Something went wrong!")
            }
        })
    }
}