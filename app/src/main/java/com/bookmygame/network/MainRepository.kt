package com.bookmygame.network

class MainRepository constructor(private val apiService: ApiService) {

    fun login(token: String, otpPin: String) = apiService.login(token, otpPinValue = otpPin)
}