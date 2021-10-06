package com.bookmygame.ui.groundOwner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bookmygame.databinding.FragmentBookingsBinding
import com.bookmygame.util.DateTimeUtil
import java.util.*

class GroundOwnerBookingsFragment : Fragment() {

    private var _binding: FragmentBookingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var selectedDate: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingsBinding.inflate(inflater, container, false)

        setUpViews()

        return binding.root
    }

    private fun setUpViews() {
        binding.apply {
            datePicker
                .setOffset(0)
                .setListener { dateSelected ->
                    Log.d(
                        "Basava",
                        "Date Selected -> $dateSelected"
                    )
                    Log.d(
                        "Basava",
                        "Date Selected: year -> ${dateSelected.year}"
                    )
                    Log.d(
                        "Basava",
                        "Date Selected: monthOfYear -> ${dateSelected.monthOfYear}"
                    )
                    Log.d(
                        "Basava",
                        "Date Selected: dayOfMonth -> ${dateSelected.dayOfMonth}"
                    )
                    val formattedDate = DateTimeUtil.format(dateSelected.toString())
                    Log.d(
                        "Basava",
                        "Date Selected: formattedDate -> $formattedDate"
                    )
                    selectedDate = formattedDate
                }
                .init()
            val now = Calendar.getInstance()
//            datePicker.setDate(DateTime(now.get(Calendar.DAY_OF_MONTH)))
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}