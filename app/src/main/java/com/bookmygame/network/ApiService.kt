package com.bookmygame.network

import com.bookmygame.network.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("api/login_type")
    fun loginType(@Body login: RequestBody): Call<LoginTypeResponse>

    @GET("api/login")
    fun login(
        @Query("token") token: String,
        @Query("otp_pin_value") otpPinValue: String
    ): Call<LoginResponse>

    @GET("api/add_ground")
    fun addGround(
        @Query("user_id") userId: String,
        @Query("mobile_no") mobileNumber: String,
        @Query("ground_name") groundName: String,
        @Query("ground_address") groundAddress: String,
        @Query("lat") groundLatitude: String,
        @Query("long") groundLongitude: String
    ): Call<AddGroundResponse>

    @POST("api/ground_ownner_grounds")
    fun groundOwnerGroundList(@Body login: RequestBody): Call<GroundOwnerGroundListResponse>

    @POST("api/user_grounds")
    fun userGroundList(@Body login: RequestBody): Call<UserGroundListResponse>

//    @POST("api/update_ground")
//    fun updateGround(@Body groundDetails: RequestBody): Call<GroundOwnerGroundUpdateResponse>

    @Multipart
    @POST("api/update_ground")
    fun updateGround(
        @Part imagePart: Array<MultipartBody.Part?>,
        @Part("user_id") userId: RequestBody,
        @Part("ground_id") groundId: RequestBody,
        @Part("ground_name") groundName: RequestBody,
        @Part("ground_location") groundLocation: RequestBody,
        @Part("pitch") pitchType: RequestBody
    ): Call<GroundOwnerGroundUpdateResponse>

    @POST("api/add_ground_slot")
    fun addSlot(@Body login: RequestBody): Call<GroundOwnerAddSlotResponse>

    @POST("api/update_slot")
    fun updateSlot(@Body login: RequestBody): Call<CommonResponse>

    @POST("api/delete_slot")
    fun removeSlot(@Body login: RequestBody): Call<CommonResponse>

    @GET("api/team_list")
    fun teams(): Call<TeamListResponse>

    @POST("api/book_slot_ground_owner")
    fun bookSlotGroundOwner(@Body login: RequestBody): Call<CommonResponse>
}
