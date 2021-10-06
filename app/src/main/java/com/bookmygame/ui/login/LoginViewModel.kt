package com.bookmygame.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bookmygame.network.ApiClient
import com.bookmygame.network.model.LoginResponse
import com.bookmygame.network.model.LoginTypeResponse
import com.bookmygame.util.Event
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val mutableLoginTypeResponse = MutableLiveData<Event<LoginTypeResponse>>()
    val loginTypeResponse: LiveData<Event<LoginTypeResponse>> get() = mutableLoginTypeResponse

    private val mutableLoginResponse = MutableLiveData<Event<LoginResponse>>()
    val loginResponse: LiveData<Event<LoginResponse>> get() = mutableLoginResponse

    private val mutableErrorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> get() = mutableErrorMessage

    fun loginType(mobileNumber: String, loginType: String) {
        Log.d("Basava", "loginType: mobileNumber -> $mobileNumber loginType -> $loginType")

//        // Create JSON using JSONObject
//        val jsonObject = JSONObject()
//        jsonObject.put("mobile_no", mobileNumber)
//        jsonObject.put("login_type", loginType)
//
//        // Convert JSONObject to String
//        val jsonObjectString = jsonObject.toString()
//
//        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
//        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("mobile_no", mobileNumber)
            .addFormDataPart("login_type", loginType)
            .build()

        val loginTypeResponse =
            ApiClient.apiService.loginType(requestBody)
        loginTypeResponse.enqueue(object : Callback<LoginTypeResponse> {
            override fun onResponse(
                call: Call<LoginTypeResponse>,
                response: Response<LoginTypeResponse>
            ) {
                Log.d(
                    "Basava",
                    "loginTypeResponse onResponse isSuccessful -> ${response.isSuccessful}"
                )
                Log.d(
                    "Basava",
                    "loginTypeResponse onResponse body -> ${response.body()}"
                )
                if (response.isSuccessful) {
                    mutableLoginTypeResponse.value = Event(response.body())
                } else {
                    Log.e("Basava", "Something went wrong")
                    Log.e("Basava", "Something went wrong: message -> ${response.message()}")
                    mutableErrorMessage.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<LoginTypeResponse>, t: Throwable) {
                Log.d("Basava", "loginTypeResponse onFailure message -> ${t.message}")
                mutableErrorMessage.value = Event(t.message ?: "Something went wrong!")
            }
        })
    }

    fun login(token: String, otpPin: String) {
        val loginResponse = ApiClient.apiService.login(token = token, otpPinValue = otpPin)
        loginResponse.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                Log.d(
                    "Basava",
                    "loginResponse onResponse isSuccessful -> ${response.isSuccessful}"
                )
                Log.d(
                    "Basava",
                    "loginResponse onResponse error -> ${response.body()?.error}"
                )
                Log.d(
                    "Basava",
                    "loginResponse onResponse body -> ${response.body()}"
                )
                Log.d(
                    "Basava",
                    "loginResponse onResponse message -> ${response.body()?.message}"
                )

                if (response.isSuccessful) {
                    mutableLoginResponse.value = Event(response.body())
                } else {
                    Log.e("Basava", "Something went wrong")
                    Log.e("Basava", "Something went wrong: message -> ${response.message()}")
                    mutableErrorMessage.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("Basava", "loginResponse onFailure message -> ${t.message}")
                mutableErrorMessage.value = Event(t.message ?: "Something went wrong!")
            }
        })
    }
}