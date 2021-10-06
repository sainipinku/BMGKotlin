package com.bookmygame.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bookmygame.MainActivity
import com.bookmygame.databinding.FragmentProfileBinding
import com.bookmygame.databinding.FragmentProfileUserBinding

class UserProfileFragment : Fragment() {

    private var _binding: FragmentProfileUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileUserBinding.inflate(inflater, container, false)

        setUpViews()
        return binding.root
    }

    private fun setUpViews() {

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}