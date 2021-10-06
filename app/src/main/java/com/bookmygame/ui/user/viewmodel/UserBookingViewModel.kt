package com.bookmygame.ui.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bookmygame.ui.user.model.UserBooking

class UserBookingViewModel : ViewModel() {

    private val groundslist: MutableLiveData<List<UserBooking>> by lazy {
        MutableLiveData<List<UserBooking>>().also {
            loadGrounds(it)
        }
    }

    fun getGroundslist(): LiveData<List<UserBooking>> {
        return groundslist
    }

    private fun loadGrounds(mutableLiveData: MutableLiveData<List<UserBooking>>) {
        // Do an asynchronous operation to fetch users.
        val groundsList = mutableListOf<UserBooking>()
        for (i in 1..10) {
            groundsList.add(UserBooking("Eden Gardens Stadium","Chenni Super King","Chenni Super King","20 overs","2.00 PM"))
        }
        mutableLiveData.value = groundsList
    }
}