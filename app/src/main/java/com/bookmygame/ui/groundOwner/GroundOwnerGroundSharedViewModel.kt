package com.bookmygame.ui.groundOwner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bookmygame.network.model.GroundOwnerGroundListResponse

class GroundOwnerGroundSharedViewModel : ViewModel() {

    private val _selectedGround = MutableLiveData<GroundOwnerGroundListResponse.Ground>()

    fun setSelectedGround(ground: GroundOwnerGroundListResponse.Ground) {
        _selectedGround.value = ground
    }

    fun getSelectedGround() = _selectedGround.value
}