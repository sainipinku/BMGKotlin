package com.bookmygame.ui.groundOwner.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bookmygame.databinding.FragmentAddImageBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddImageBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentAddImageBinding? = null
    private val binding get() = _binding!!

    private var listener: AddImageListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddImageBinding.inflate(inflater, container, false)

        setUpViews()

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setUpViews() {
        binding.apply {
            camera.setOnClickListener {
                listener?.fromCamera()
                dismiss()
            }

            gallery.setOnClickListener {
                listener?.fromGallery()
                dismiss()
            }
        }
    }

    fun setOnAddImageListener(listener: AddImageListener) {
        this.listener = listener
    }

    interface AddImageListener {
        fun fromCamera()
        fun fromGallery()
    }
}