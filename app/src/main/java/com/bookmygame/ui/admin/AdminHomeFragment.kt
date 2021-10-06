package com.bookmygame.ui.admin

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bookmygame.MainActivity
import com.bookmygame.R
import com.bookmygame.databinding.FragmentHomeAdminBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class AdminHomeFragment : Fragment() {

    private var _binding: FragmentHomeAdminBinding? = null

    private val binding get() = _binding!!

    private val args by navArgs<AdminHomeFragmentArgs>()

    private lateinit var adminViewModel: AdminViewModel

    //    private lateinit var placesClient: PlacesClient
//    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private var groundName: String? = null
    private var groundAddress: String? = null
    private var latLng: LatLng? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeAdminBinding.inflate(inflater, container, false)
        adminViewModel = ViewModelProvider(this).get(AdminViewModel::class.java)

        setUpViews()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Log.d("Basava", "AdminHomeFragment onCreate")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        Log.d("Basava", "AdminHomeFragment onCreateOptionsMenu")
        (activity as MainActivity).addAdminMenuItems()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setUpViews() {
        if (Places.isInitialized().not()) {
            val apiKey = resources.getString(R.string.api_key)
            Places.initialize(requireContext(), apiKey)

//            placesClient = PlacesClient.createClient(this)
        }

        binding.apply {

            // Initialize the AutocompleteSupportFragment.
            initAutocompleteSupportFragment()

            mobileNumber.addTextChangedListener {
                if (it.isNullOrEmpty().not()) layoutMobileNumber.error = ""
            }
//            groundName.addTextChangedListener {
//                if (it.isNullOrEmpty().not()) layoutGroundName.error = ""
//            }

            save.setOnClickListener {

                val mobileNumber = mobileNumber.text.toString().trim()
                if (mobileNumber.isEmpty()) {
                    layoutMobileNumber.error = "Please Enter Phone Number"
                    return@setOnClickListener
                }
//                val groundName = groundName.text.toString().trim()
//                if (groundName.isEmpty()) {
//                    layoutGroundName.error = "Please Enter Ground Name"
//                    return@setOnClickListener
//                }
                if (this@AdminHomeFragment.groundName.isNullOrEmpty() || groundAddress.isNullOrEmpty() || latLng == null) {
                    showToast("Please enter Ground name")
                    return@setOnClickListener
                }

                adminViewModel.addGround(
                    userId = args.userId,
                    mobileNumber = mobileNumber,
                    groundName = this@AdminHomeFragment.groundName!!,
                    groundAddress = groundAddress!!,
                    groundLocation = latLng!!
                )

                it.isEnabled = false

//                // Set the fields to specify which types of place data to
//                // return after the user has made a selection.
//                val fields = listOf(Place.Field.ID, Place.Field.NAME)
//
//                // Start the autocomplete intent.
//                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
//                    .build(requireContext())
//                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
//            when (resultCode) {
//                Activity.RESULT_OK -> {
//                    data?.let {
//                        val place = Autocomplete.getPlaceFromIntent(data)
//                        Log.i("Basava", "Place: ${place.name}, ${place.id}")
//                    }
//                }
//                AutocompleteActivity.RESULT_ERROR -> {
//                    // TODO: Handle the error.
//                    data?.let {
//                        val status = Autocomplete.getStatusFromIntent(data)
//                        Log.i("Basava", status.statusMessage)
//                    }
//                }
//                Activity.RESULT_CANCELED -> {
//                    // The user canceled the operation.
//                }
//            }
//            return
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }

    private fun initAutocompleteSupportFragment() {
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setHint("Search Ground here")
        autocompleteFragment.setCountry("IN")
        autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT)
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.ADDRESS_COMPONENTS,
                Place.Field.PHOTO_METADATAS,
                Place.Field.LAT_LNG,
            )
        )

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Log.d("Basava", "onPlaceSelected")
                Log.d("Basava", "id: ${place.id}")
                Log.d("Basava", "name: ${place.name}")
                Log.d("Basava", "isOpen: ${place.isOpen}")
                Log.d("Basava", "address: ${place.address}")
                Log.d("Basava", "addressComponents: ${place.addressComponents}")
                Log.d("Basava", "latLng: ${place.latLng}")
                Toast.makeText(
                    context,
                    place.name,
                    Toast.LENGTH_SHORT
                ).show()
                groundName = place.name
                groundAddress = place.address
                latLng = place.latLng
                binding.groundName.setText(place.name)
                binding.save.isEnabled = true
            }

            override fun onError(status: Status) {
                Log.d("Basava", "onError")
                Log.d("Basava", "statusCode: ${status.statusCode}")
                Log.d("Basava", "statusMessage: ${status.statusMessage}")
                Toast.makeText(
                    context,
                    status.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

//        autocompleteFragment.view.findViewById<Button>(R.id.clo)
//        autocompleteFragment.
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}