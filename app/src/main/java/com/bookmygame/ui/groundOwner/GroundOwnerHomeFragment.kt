package com.bookmygame.ui.groundOwner

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bookmygame.FragmentCallbacks
import com.bookmygame.MainActivity
import com.bookmygame.R
import com.bookmygame.databinding.FragmentHomeGroundOwnerBinding
import com.bookmygame.ui.groundOwner.adapter.GroundOwnerGroundsAdapter
import com.bookmygame.util.SPConstants

class GroundOwnerHomeFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentHomeGroundOwnerBinding? = null

    private val binding get() = _binding!!

//    private val args by navArgs<GroundOwnerHomeFragmentArgs>()

    private lateinit var model: GroundOwnerHomeViewModel

    private val sharedViewModel: GroundOwnerGroundSharedViewModel by activityViewModels()

    private  var fragmentCallback: FragmentCallbacks = TODO()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("Basava", "GroundOwnerHomeFragment onAttach")
        fragmentCallback = context as FragmentCallbacks
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
        Log.d("Basava", "GroundOwnerHomeFragment onCreate")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        Log.d("Basava", "GroundOwnerHomeFragment onCreateOptionsMenu")
        (activity as MainActivity).addGroundOwnerMenuItems()

        inflater.inflate(R.menu.menu_ground_owner_home, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeGroundOwnerBinding.inflate(inflater, container, false)

        setUpViews()

        model = ViewModelProvider(this).get(GroundOwnerHomeViewModel::class.java)

        model.groundOwnerGroundListResponse.observe(viewLifecycleOwner, { event ->
            Log.d("Basava", "GroundOwnerHomeFragment loginTypeResponse.observe")
            event.getContentIfNotHandled()?.let { response ->
                if (response.error.not()) {
                    Log.d("Basava", "data ${response.grounds}")
                    // update UI
                    binding.recyclerView.apply {
                        adapter = GroundOwnerGroundsAdapter(
                            groundsList = response.grounds,
                            onUpdateDetailsClick = { ground ->
                                Log.d("Basava", "onUpdateDetailsClick1")
                                sharedViewModel.setSelectedGround(ground)
                                findNavController().navigate(R.id.navigate_to_ground_detail_screen)
                            },
                            onAddSlotClick = { ground ->
                                Log.d("Basava", "onAddSlotClick1")
                                sharedViewModel.setSelectedGround(ground)
                                findNavController().navigate(R.id.navigate_to_slots_screen)
                            })
                    }
                } else {
                    showToast(response.message)
//                    binding.layoutMobileNumber.error = response.message
                }
                fragmentCallback.hideProgressBar()
            }
        })

        model.errorMessage.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let { errorMessage ->
                showToast(errorMessage)
                fragmentCallback.hideProgressBar()
            }
        })

        val userId = (activity as MainActivity).getValue(SPConstants.USED_ID)
        userId?.let {
            model.getGrounds(it)
            fragmentCallback.showProgressBar()
        } ?: error("User id not available")

        return binding.root
    }

    private fun setUpViews() {
        binding.apply {
            recyclerView.adapter = GroundOwnerGroundsAdapter(
                emptyList(),
                onUpdateDetailsClick = {
                    Log.d("Basava", "onAddSlotClick")
                },
                onAddSlotClick = {
                    Log.d("Basava", "onAddSlotClick")
                })
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