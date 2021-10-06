package com.bookmygame.ui.user

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bookmygame.FragmentCallbacks

import com.bookmygame.MainActivity
import com.bookmygame.R
import com.bookmygame.databinding.FragmentHomeUserBinding
import com.bookmygame.ui.groundOwner.Slot
import com.bookmygame.ui.groundOwner.adapter.GroundOwnerGroundsAdapter
import com.bookmygame.ui.user.adapter.UserGroundsAdapter
import com.bookmygame.ui.user.viewmodel.UserHomeViewModel
import com.bookmygame.util.SPConstants

class UserHomeFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentHomeUserBinding? = null

    private val binding get() = _binding!!

    private lateinit var model: UserHomeViewModel
   // private  var fragmentCallback: FragmentCallbacks = TODO()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("Basava", "GroundOwnerHomeFragment onAttach")
       // fragmentCallback = context as FragmentCallbacks
//        requireActivity().window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().window.statusBarColor =
                    ContextCompat.getColor(context, R.color.colorPrimary)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Log.d("Basava", "UserHomeFragment onCreate")
    }

 override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        Log.d("Basava", "UserHomeFragment onCreateOptionsMenu")
        menu.clear()
        inflater.inflate(R.menu.menu_user_home, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeUserBinding.inflate(inflater, container, false)

        setUpViews()

        model = ViewModelProvider(this).get(UserHomeViewModel::class.java)
       model.groundUserListResponse.observe(viewLifecycleOwner, { event ->
           Log.d("Basava", "GroundOwnerHomeFragment loginTypeResponse.observe")
           event.getContentIfNotHandled()?.let { response ->
               if (response.message=="success") {
                   Log.d("Basava", "data ${response.grounds}")
                   // update UI
                   binding.recyclerView.apply {
                       adapter = UserGroundsAdapter(
                               groundsList = response.grounds,
                               onUpdateDetailsClick = { ground ->
                                   Log.d("Basava", "onUpdateDetailsClick1")
                                   findNavController().navigate(R.id.nav_user_ground_detail)
                               },)
                   }
               } else {
                   showToast(response.message)
//                    binding.layoutMobileNumber.error = response.message
               }
              // fragmentCallback.hideProgressBar()
           }
       })

       model.errorMessage.observe(viewLifecycleOwner, { event ->
           event.getContentIfNotHandled()?.let { errorMessage ->
               showToast(errorMessage)
               //fragmentCallback.hideProgressBar()
           }
       })

       val userId = (activity as MainActivity).getValue(SPConstants.USED_ID)
       userId?.let {
           model.getGrounds(it)
           //fragmentCallback.showProgressBar()
       } ?: error("User id not available")

       /*  model.getGrounds().observe(viewLifecycleOwner, { grounds ->
             Log.d("Basava", "grounds -> $grounds")
             // update UI
             binding.recyclerView.apply {
                 adapter = UserGroundsAdapter(grounds)
             }
         })*/

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).addUserMenuItems()
    }

    private fun setUpViews() {
        binding.apply {

            recyclerView.adapter = UserGroundsAdapter(emptyList(),
                    onUpdateDetailsClick = {
                        Log.d("Basava", "onAddSlotClick")
                    },
                    )
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
       // (binding.recyclerView.adapter as UserGroundsAdapter).filter.filter(newText)
        return false
    }
}