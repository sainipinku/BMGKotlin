package com.bookmygame.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bookmygame.MainActivity
import com.bookmygame.R
import com.bookmygame.databinding.FragmentSplashBinding
import com.bookmygame.util.SPConstants

class FragmentSplash : Fragment() {

    private var _binding: FragmentSplashBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
//        Handler().postDelayed({
//            (activity as MainActivity).getValue(SPConstants.ROLE)?.let {
//                goToHomeScreen()
//            } ?: findNavController().navigate(R.id.navigate_to_login_screen)
//        }, 3000)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        Handler().postDelayed({
            (activity as MainActivity).getValue(SPConstants.ROLE)?.let {
                goToHomeScreen()
            } ?: findNavController().navigate(R.id.navigate_to_login_screen)
        }, 3000)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(context, R.color.colorPrimary)
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).apply {
            supportActionBar?.hide()
            hideBottomNavigationBar()
        }
    }

    override fun onDestroyView() {
        Log.d("Basava", "FragmentSplash onDestroyView")
        _binding = null
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
//        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        (requireActivity() as MainActivity).supportActionBar?.show()
    }

    private fun goToHomeScreen() {
        val roleName = (activity as MainActivity).getValue(SPConstants.ROLE)
        val userId = (activity as MainActivity).getValue(SPConstants.USED_ID)

        Log.d("Basava", "OTPFragment goToHomeScreen: roleName -> $roleName")
        when (roleName) {
            "admin" -> {
                val bundle = bundleOf("user_id" to userId)
                findNavController().navigate(R.id.navigate_to_admin_home_screen, bundle)
            }
            "ground_owner" -> {
//                val bundle = bundleOf("user_id" to userId)
                findNavController().navigate(R.id.navigate_to_ground_owner_home_screen/*, bundle*/)
            }
            "user" -> {
                val bundle = bundleOf("user_id" to userId)
                findNavController().navigate(R.id.navigate_to_user_home_screen, bundle)
            }
            else -> {
                error("No role provided")
            }
        }
    }
}