package com.bookmygame.ui.groundOwner

import `in`.madapps.placesautocomplete.PlaceAPI
import `in`.madapps.placesautocomplete.adapter.PlacesAutoCompleteAdapter
import `in`.madapps.placesautocomplete.listener.OnPlacesDetailsListener
import `in`.madapps.placesautocomplete.model.Place
import `in`.madapps.placesautocomplete.model.PlaceDetails
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookmygame.FragmentCallbacks
import com.bookmygame.MainActivity
import com.bookmygame.R
import com.bookmygame.databinding.FragmentGroundOwnerGroundDetailBinding
import com.bookmygame.network.model.GroundOwnerGroundListResponse
import com.bookmygame.ui.groundOwner.adapter.GroundOwnerGroundImageAdapter
import com.bookmygame.ui.groundOwner.bottomsheet.AddImageBottomSheet
import com.bookmygame.util.hideKeyboard
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.File

class GroundOwnerGroundDetailFragment : Fragment(), AddImageBottomSheet.AddImageListener {

    private var _binding: FragmentGroundOwnerGroundDetailBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: GroundOwnerGroundSharedViewModel by activityViewModels()

    private lateinit var viewModel: GroundOwnerGroundDetailViewModel

    private lateinit var ground: GroundOwnerGroundListResponse.Ground

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("Basava", "${it.key}=${it.value}")
            }
        }

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
//                previewImage.setImageURI(uri)
                    Log.d("Basava", "Camera uri: $uri")
                    Log.d("Basava", "Camera uri path: ${uri.path}")
                    (binding.recyclerViewGroundImage.adapter as GroundOwnerGroundImageAdapter).addImage(
                        GroundImageSource.Local(uri)
                    )
                    showAddImageView()
                }
            } else {
                Log.e("Basava", "Take picture failed!")
            }
        }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
//                previewImage.setImageURI(uri)
                Log.d("Basava", "Camera uri: $uri")
                Log.d("Basava", "Camera uri path: ${uri.path}")
                (binding.recyclerViewGroundImage.adapter as GroundOwnerGroundImageAdapter).addImage(
                    GroundImageSource.Local(it)
                )
                showAddImageView()
            }
        }

    private var latestTmpUri: Uri? = null

//    private fun getGround(): GroundOwnerGroundListResponse.Ground =
//        sharedViewModel.getSelectedGround()!!.copy()

    private lateinit var fragmentCallback: FragmentCallbacks

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentCallback = context as FragmentCallbacks
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroundOwnerGroundDetailBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(GroundOwnerGroundDetailViewModel::class.java)

        ground = sharedViewModel.getSelectedGround()!!.copy()
        setListeners()
        setUpViews()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ground = sharedViewModel.getSelectedGround()!!.copy()
        Log.d("Basava", "GroundOwnerGroundDetailFragment onViewCreated: ground -> $ground")
        fragmentCallback.hideBottomNavigationBar()
    }

    override fun onDestroyView() {
        hideKeyboard()
        _binding = null
        super.onDestroyView()
    }

    private fun setListeners() {
        viewModel.groundOwnerUpdateGroundResponse.observe(viewLifecycleOwner, { event ->
            Log.d("Basava", "GroundOwnerHomeFragment loginTypeResponse.observe")
            event.getContentIfNotHandled()?.let { response ->
                if (response.error.not()) {
                    Log.d("Basava", "data ${response.message}")
                    // update UI
                    showToast(response.message)
                } else {
                    showToast(response.message)
//                    binding.layoutMobileNumber.error = response.message
                }
                (activity as? MainActivity)?.hideProgressBar()
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let { errorMessage ->
                showToast(errorMessage)
                (activity as? MainActivity)?.hideProgressBar()
            }
        })
    }

    private fun setUpViews() {
        binding.apply {
            val layoutManager = LinearLayoutManager(context)
            recyclerViewGroundImage.layoutManager = layoutManager

            val horizontalManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewGroundImage.layoutManager = horizontalManager
            val groundImages = ground.images
            val imagesToBeLoaded = viewModel.prepareImagesToBeLoaded(groundImages)
            recyclerViewGroundImage.adapter =
                GroundOwnerGroundImageAdapter(imagesToBeLoaded as MutableList<GroundImageSource>)
            if (imagesToBeLoaded.isEmpty().not()) showAddImageView()

            layoutAddImages.setOnClickListener {
                checkPermission()
            }

            addImage.setOnClickListener {
                checkPermission()
            }

            edittextGroundName.setText(ground.groundName)
            autocompleteLocation.setText(ground.groundAddress)
            spinnerPitchType.setSelection(
                (spinnerPitchType.adapter as ArrayAdapter<String>).getPosition(
                    ground.pitchType
                )
            )
            spinnerPitchType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Log.d("Basava", "Pitch type selected: selectedItem -> ${parent?.selectedItem}")
                    ground.pitchType = parent?.selectedItem.toString()
//                    getGround().copy(pitchId = parent?.selectedItem.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.d("Basava", "Pitch type onNothingSelected")
                }
            }

            val apiKey = resources.getString(R.string.api_key)
            val placeApi = PlaceAPI.Builder().apiKey(apiKey).build(requireContext())
            autocompleteLocation.setAdapter(PlacesAutoCompleteAdapter(requireContext(), placeApi))

            autocompleteLocation.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    val place =
                        parent.getItemAtPosition(position) as Place
                    Log.d("Basava", "place id -> ${place.id}")
                    Log.d("Basava", "place description -> ${place.description}")
                    autocompleteLocation.setText(place.description)
                    placeApi.fetchPlaceDetails(place.id, object : OnPlacesDetailsListener {
                        override fun onError(errorMessage: String) {
                            Log.e("Basava", "onError: errorMessage -> $errorMessage")
                        }

                        override fun onPlaceDetailsFetched(placeDetails: PlaceDetails) {
                            Log.d("Basava", "onPlaceDetailsFetched: placeDetails -> $placeDetails")
                        }
                    })
                }

            update.setOnClickListener {
                hideKeyboard()
                (activity as MainActivity).showProgressBar()
                val imagePaths =
                    (recyclerViewGroundImage.adapter as GroundOwnerGroundImageAdapter).getImageUriPaths()
//                val imageUris =
//                    (recyclerViewGroundImage.adapter as GroundOwnerGroundImageAdapter).getImageUris()
//                getGround().copy(
//                    groundName = edittextGroundName.text.toString().trim(),
//                    groundAddress = edittextLocation.text.toString().trim()
//                )
                ground.groundName = edittextGroundName.text.toString().trim()
                ground.groundAddress = autocompleteLocation.text.toString().trim()
                viewModel.updateGround(ground, imagePaths)
            }
        }
    }

    private fun initImageSourceSelection() {
        val bottomSheet = AddImageBottomSheet()
        bottomSheet.setOnAddImageListener(this)
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    override fun fromCamera() {
//        takeImage()
//        ImagePicker.with(this)
//            .compress(1024)         //Final image size will be less than 1 MB(Optional)
//            .maxResultSize(
//                1080,
//                1080
//            )  //Final image resolution will be less than 1080 x 1080(Optional)
//            .createIntent { intent ->
//                startForProfileImageResult.launch(intent)
//            }
        ImagePicker.with(this)
            .cameraOnly()    //User can only capture image using Camera
            .start()
    }

    override fun fromGallery() {
        // TODO: 16/09/21 Images taken from the Gallery not uploading to server
//        selectImageFromGallery()
//        ImagePicker.with(this)
//            .compress(1024)         //Final image size will be less than 1 MB(Optional)
//            .maxResultSize(
//                1080,
//                1080
//            )  //Final image resolution will be less than 1080 x 1080(Optional)
//            .createIntent { intent ->
//                startForProfileImageResult.launch(intent)
//            }
        ImagePicker.with(this)
            .galleryOnly()    //User can only select image from Gallery
            .start()
    }

    private fun showAddImageView() {
        binding.apply {
            layoutAddImages.visibility = View.GONE
            groupAddImage.visibility = View.VISIBLE
        }
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                Log.d("Basava", "We have the permission!")
                initImageSourceSelection()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.
// showInContextUI(...)
                Log.d("Basava", "Please provide permission")
                requestPermissions()
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissions()
            }
        }
    }

    private fun requestPermissions() {
        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
    }

    private fun takeImage() {
//        val file = File(requireActivity().filesDir, "picFromCamera")
//        val uri = FileProvider.getUriForFile(
//            requireContext(),
//            requireContext().applicationContext.packageName.plus(".provider"),
//            file
//        )
//        takeImageResult.launch(uri)
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun getTmpFileUri(): Uri {
//        val tmpFile =
//            File.createTempFile("tmp_image_file", ".png", requireActivity().cacheDir).apply {
//                createNewFile()
//                deleteOnExit()
//            }
//
//        return FileProvider.getUriForFile(
//            requireActivity().applicationContext,
//            "${BuildConfig.APPLICATION_ID}.provider",
//            tmpFile
//        )
        val file = File(requireActivity().filesDir, "picFromCamera")
        val uri = FileProvider.getUriForFile(
            requireContext(),
            requireContext().applicationContext.packageName.plus(".provider"),
            file
        )
        return uri
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

//                mProfileUri = fileUri
//                imgProfile.setImageURI(fileUri)
                Log.d("Basava", "path -> ${fileUri.path}")
                (binding.recyclerViewGroundImage.adapter as GroundOwnerGroundImageAdapter).addImage(
                    GroundImageSource.Local(fileUri)
                )
                showAddImageView()
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            // Use Uri object instead of File to avoid storage permissions
//            imgProfile.setImageURI(fileUri)
            Log.d("Basava", "path -> ${uri.path}")
            (binding.recyclerViewGroundImage.adapter as GroundOwnerGroundImageAdapter).addImage(
                GroundImageSource.Local(uri)
            )
            showAddImageView()
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}