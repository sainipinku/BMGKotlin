package com.bookmygame.ui.groundOwner

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bookmygame.network.ApiClient
import com.bookmygame.network.model.CommonResponse
import com.bookmygame.network.model.GroundOwnerAddSlotResponse
import com.bookmygame.network.model.GroundOwnerGroundListResponse
import com.bookmygame.network.model.TeamListResponse
import com.bookmygame.util.Event
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroundOwnerSlotViewModel : ViewModel() {

    private val mutableSlots = MutableLiveData<List<Slot>>()
    val slots: LiveData<List<Slot>> get() = mutableSlots

    private val mutableAddSlotResponse = MutableLiveData<Event<GroundOwnerAddSlotResponse>>()
    val addSlotResponse: LiveData<Event<GroundOwnerAddSlotResponse>> get() = mutableAddSlotResponse

    private val mutableUpdateSlotResponse = MutableLiveData<Event<CommonResponse>>()
    val updateSlotResponse: LiveData<Event<CommonResponse>> get() = mutableUpdateSlotResponse

    private val mutableRemoveSlotResponse = MutableLiveData<Event<CommonResponse>>()
    val removeSlotResponse: LiveData<Event<CommonResponse>> get() = mutableRemoveSlotResponse

    private val mutableTeamListResponse = MutableLiveData<Event<TeamListResponse>>()
    val teamListResponse: LiveData<Event<TeamListResponse>> get() = mutableTeamListResponse

    private val mutableBookSlotResponse = MutableLiveData<Event<CommonResponse>>()
    val bookSlotResponse: LiveData<Event<CommonResponse>> get() = mutableBookSlotResponse

    private val mutableErrorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> get() = mutableErrorMessage

    fun loadSlots(slots: List<GroundOwnerGroundListResponse.Ground.Slot>?) {
        Log.d("Basava", "loadSlots: $slots")
        // Do an asynchronous operation to fetch users.
        val slotsList = mutableListOf<Slot>()

        slots?.let {
            for (slot in slots) {
                val slotToBe =
                    Slot(
                        id = slot.slotId,
                        noOfOvers = slot.noOfOvers,
                        fromDate = slot.fromDate,
                        toDate = slot.toDate,
                        startTime = slot.fromTime.orEmpty(),
                        endTime = slot.toTime.orEmpty(),
                        price = slot.price,
                        ballType = slot.ballType
                    )
                slot.bookings.getOrNull(0)?.let { team1 ->
                    slotToBe.team1 = Slot.Team(null, team1.teamName)
                }
                slot.bookings.getOrNull(1)?.let { team2 ->
                    slotToBe.team2 = Slot.Team(null, team2.teamName)
                }
                slotsList.add(slotToBe)
            }
        }
        mutableSlots.value = slotsList
    }

    fun addSlot(userId: String, groundId: String, slot: Slot) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .addFormDataPart("ground_id", groundId)
            .addFormDataPart("from_date", slot.fromDate)
            .addFormDataPart("to_date", slot.toDate)
            .addFormDataPart("from_time", slot.startTime)
            .addFormDataPart("to_time", slot.endTime)
            .addFormDataPart("no_of_overs", slot.noOfOvers)
            .addFormDataPart("ball_type", slot.ballType)
            .addFormDataPart("price", slot.price)
            .build()

        val addSlotResponse =
            ApiClient.apiService.addSlot(requestBody)
        addSlotResponse.enqueue(object : Callback<GroundOwnerAddSlotResponse> {
            override fun onResponse(
                call: Call<GroundOwnerAddSlotResponse>,
                response: Response<GroundOwnerAddSlotResponse>
            ) {
                Log.d(
                    "Basava",
                    "addSlot onResponse isSuccessful -> ${response.isSuccessful}"
                )
                Log.d(
                    "Basava",
                    "addSlot onResponse body -> ${response.body()}"
                )
                if (response.isSuccessful) {
                    mutableAddSlotResponse.value = Event(response.body())
                } else {
                    Log.e("Basava", "Something went wrong")
                    Log.e("Basava", "Something went wrong: message -> ${response.message()}")
                    mutableErrorMessage.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<GroundOwnerAddSlotResponse>, t: Throwable) {
                Log.d("Basava", "addSlot onFailure message -> ${t.message}")
                mutableErrorMessage.value = Event(t.message ?: "Something went wrong!")
            }
        })
    }

    fun updateSlot(slot: Slot) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("slot_id", slot.id)
            .addFormDataPart("from_date", slot.fromDate)
            .addFormDataPart("to_date", slot.toDate)
//            .addFormDataPart("from_time", slot.startTime)
//            .addFormDataPart("to_time", slot.endTime)
            .addFormDataPart("no_of_overs", slot.noOfOvers)
            .addFormDataPart("ball_type", slot.ballType)
            .addFormDataPart("price", slot.price)
            .build()

        val addSlotResponse =
            ApiClient.apiService.updateSlot(requestBody)
        addSlotResponse.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(
                    "Basava",
                    "updateSlot onResponse isSuccessful -> ${response.isSuccessful}"
                )
                Log.d(
                    "Basava",
                    "updateSlot onResponse body -> ${response.body()}"
                )
                if (response.isSuccessful) {
                    mutableUpdateSlotResponse.value = Event(response.body())
                } else {
                    Log.e("Basava", "Something went wrong")
                    Log.e("Basava", "Something went wrong: message -> ${response.message()}")
                    mutableErrorMessage.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.d("Basava", "updateSlot onFailure message -> ${t.message}")
                mutableErrorMessage.value = Event(t.message ?: "Something went wrong!")
            }
        })
    }

    fun removeSlot(userId: String, slotId: String) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .addFormDataPart("slot_id", slotId)
            .build()

        val addSlotResponse =
            ApiClient.apiService.removeSlot(requestBody)
        addSlotResponse.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(
                    "Basava",
                    "RemoveSlot onResponse isSuccessful -> ${response.isSuccessful}"
                )
                Log.d(
                    "Basava",
                    "RemoveSlot onResponse body -> ${response.body()}"
                )
                if (response.isSuccessful) {
                    mutableRemoveSlotResponse.value = Event(response.body())
                } else {
                    Log.e("Basava", "Something went wrong")
                    Log.e("Basava", "Something went wrong: message -> ${response.message()}")
                    mutableErrorMessage.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.d("Basava", "loginTypeResponse onFailure message -> ${t.message}")
                mutableErrorMessage.value = Event(t.message ?: "Something went wrong!")
            }
        })
    }

    fun getTeams() {
        val response = ApiClient.apiService.teams()
        response.enqueue(object : Callback<TeamListResponse> {
            override fun onResponse(
                call: Call<TeamListResponse>,
                response: Response<TeamListResponse>
            ) {
                Log.d(
                    "Basava",
                    "getTeams onResponse isSuccessful -> ${response.isSuccessful}"
                )
                Log.d(
                    "Basava",
                    "getTeams onResponse body -> ${response.body()}"
                )
                if (response.isSuccessful) {
                    mutableTeamListResponse.value = Event(response.body())
                } else {
                    Log.e("Basava", "Something went wrong")
                    Log.e("Basava", "Something went wrong: message -> ${response.message()}")
                    mutableErrorMessage.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<TeamListResponse>, t: Throwable) {
                Log.d("Basava", "getTeams onFailure message -> ${t.message}")
                mutableErrorMessage.value = Event(t.message ?: "Something went wrong!")
            }
        })
    }

    fun bookSlot(userId: String, groundId: String, slotId: String, team: Slot.Team) {
        // If selecting the existing team, just pass the team id
        // else if adding a new team which is not registered in the app, pass team name (don't pass team id)
        val teamIdOrNameKey = if (team.id.isNullOrEmpty().not()) "team_id" else "team_name"
        val teamIdOrNameValue = if (team.id.isNullOrEmpty().not()) team.id else team.name

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .addFormDataPart("ground_id", groundId)
            .addFormDataPart("slot_id", slotId)
            .addFormDataPart(teamIdOrNameKey, teamIdOrNameValue)
            .addFormDataPart("amount_paid", "") // TODO: 26/09/21 Need interface to collect this?
            .addFormDataPart("payment_method", "Offline") // TODO: 26/09/21 Define payments methods
            .addFormDataPart("payment_type", "") // TODO: 26/09/21 Need interface to collect this?
            .build()

        val addSlotResponse =
            ApiClient.apiService.bookSlotGroundOwner(requestBody)
        addSlotResponse.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(
                    "Basava",
                    "bookSlot onResponse isSuccessful -> ${response.isSuccessful}"
                )
                Log.d(
                    "Basava",
                    "bookSlot onResponse body -> ${response.body()}"
                )
                if (response.isSuccessful) {
                    mutableBookSlotResponse.value = Event(response.body())
                } else {
                    Log.e("Basava", "Something went wrong")
                    Log.e("Basava", "Something went wrong: message -> ${response.message()}")
                    mutableErrorMessage.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.d("Basava", "bookSlot onFailure message -> ${t.message}")
                mutableErrorMessage.value = Event(t.message ?: "Something went wrong!")
            }
        })
    }
}