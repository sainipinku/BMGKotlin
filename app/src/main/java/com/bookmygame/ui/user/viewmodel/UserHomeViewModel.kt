package com.bookmygame.ui.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import com.bookmygame.network.ApiClient
import com.bookmygame.network.model.GroundOwnerGroundListResponse
import com.bookmygame.network.model.UserGroundListResponse
import com.bookmygame.util.Event
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserHomeViewModel : ViewModel() {

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

    private val mutableUserGroundListResponse =
            MutableLiveData<Event<UserGroundListResponse>>()
    val groundUserListResponse: LiveData<Event<UserGroundListResponse>> get() = mutableUserGroundListResponse

    private val mutableErrorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> get() = mutableErrorMessage

    fun getGrounds(userId: String) {
        Log.d("Basava", "getGrounds: userId -> $userId")

        val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
                .build()

        val groundsResponse =
                ApiClient.apiService.userGroundList(requestBody)
        groundsResponse.enqueue(object : Callback<UserGroundListResponse> {
            override fun onResponse(
                    call: Call<UserGroundListResponse>,
                    response: Response<UserGroundListResponse>
            ) {
                Log.d(
                        "Basava",
                        "UserGroundListResponse onResponse isSuccessful -> ${response.isSuccessful}"
                )
                Log.d(
                        "Basava",
                        "UserGroundListResponse onResponse body -> ${response.body()}"
                )
                if (response.isSuccessful) {
                    mutableUserGroundListResponse.value = Event(response.body())
                } else {
                    Log.e("Basava", "Something went wrong")
                    Log.e("Basava", "Something went wrong: message -> ${response.message()}")
                    mutableErrorMessage.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<UserGroundListResponse>, t: Throwable) {
                Log.d("Basava", "UserGroundListResponse onFailure message -> ${t.message}")
                mutableErrorMessage.value = Event(t.message ?: "Something went wrong!")
            }
        })
    }
}
/*
class UserHomeViewModel : ViewModel() {

    private val grounds: MutableLiveData<List<Ground>> by lazy {
        MutableLiveData<List<Ground>>().also {
            loadGrounds(it)
        }
    }

    fun getGrounds(): LiveData<List<Ground>> {
        return grounds
    }

    private fun loadGrounds(mutableLiveData: MutableLiveData<List<Ground>>) {
        // Do an asynchronous operation to fetch users.
        val groundsList = mutableListOf<Ground>()
        for (i in 1..10) {
            groundsList.add(Ground("Eden Gardens Stadium","West bengal"))
        }
        mutableLiveData.value = groundsList
    }
}*/
