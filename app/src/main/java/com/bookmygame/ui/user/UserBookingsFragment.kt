package com.bookmygame.ui.user

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bookmygame.MainActivity
import com.bookmygame.R
import com.bookmygame.databinding.FragmentBookingsUserBinding
import com.bookmygame.ui.groundOwner.adapter.GroundOwnerGroundsAdapter
import com.bookmygame.ui.user.adapter.UserBookingAdapter
import com.bookmygame.ui.user.model.UserBooking
import com.bookmygame.ui.user.viewmodel.UserBookingViewModel
import com.bookmygame.util.DateTimeUtil
import java.util.*

class UserBookingsFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentBookingsUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var userbooking: UserBooking
    private lateinit var selectedDate: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingsUserBinding.inflate(inflater, container, false)

        setUpViews()
        var userbooking = ViewModelProvider(this).get(UserBookingViewModel::class.java)
        userbooking.getGroundslist().observe(viewLifecycleOwner) { grounds ->
            Log.d("Basava", "grounds -> $grounds")
            // update UI
            binding.recyclerView.apply {
                adapter = UserBookingAdapter(grounds)
            }
            //binding.toolbar.title = "test"
            /*binding.menuBtn.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    if (view != null) {
                        (activity as MainActivity).openCloseNavigationDrawer(view)
                    }
                }

            })*/

        }

        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Log.d("Basava", "GroundOwnerHomeFragment onCreate")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        Log.d("Basava", "GroundOwnerHomeFragment onCreateOptionsMenu")
//        (activity as MainActivity).addGroundOwnerMenuItems()

        inflater.inflate(R.menu.menu_user_home, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
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
    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d("Basava", "onQueryTextSubmit: query -> $query")
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d("Basava", "onQueryTextChange: newText -> $newText")
        (binding.recyclerView.adapter as GroundOwnerGroundsAdapter).filter.filter(newText)
        return false
    }
}