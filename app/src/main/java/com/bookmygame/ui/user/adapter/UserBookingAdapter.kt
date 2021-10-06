package com.bookmygame.ui.user.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookmygame.R
import com.bookmygame.databinding.FragmentBookingSlotBinding
import com.bookmygame.databinding.FragmentBookingUserBinding
import com.bookmygame.ui.user.model.UserBooking

class UserBookingAdapter(private var groundsList: List<UserBooking>) :
    RecyclerView.Adapter<BookingViewHolder>(){

    private var groundFilterList = listOf<UserBooking>()

    init {
        groundFilterList = groundsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        return BookingViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_booking_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(groundFilterList[position])
    }

    override fun getItemCount(): Int = groundFilterList.size


}

class BookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = FragmentBookingUserBinding.bind(view)

    init {
        binding.root.setOnClickListener {
            Log.d("Basava", "Ground clicked")
        }
    }

    fun bind(ground: UserBooking?) {
        binding.apply {
            //team1Name.text = ground?.team1
            //team2Name.text = ground?.team2
            groundName.text = ground?.name
            overs.text =ground?.over
            time.text = ground?.time
        }
    }
}