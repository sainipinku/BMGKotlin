package com.bookmygame.ui.login

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bookmygame.MainActivity
import com.bookmygame.R
import com.bookmygame.databinding.FragmentPinBinding
import com.bookmygame.network.model.LoginResponse
import com.bookmygame.util.hideKeyboard

class PINFragment : Fragment() {

    private var _binding: FragmentPinBinding? = null

    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var otp: String

    private val args by navArgs<PINFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Log.d("Basava", "PINFragment onCreate")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        Log.d("Basava", "PINFragment onCreateOptionsMenu")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        _binding = FragmentPinBinding.inflate(inflater, container, false)

        loginViewModel.loginResponse.observe(viewLifecycleOwner, { event ->
            Log.d("Basava", "PINFragment loginResponse.observe")
            event.getContentIfNotHandled()?.let { response ->
                if (response.error.not() && response.data.isNullOrEmpty().not()) {
                    Log.d("Basava", "Going to Home screen!")
//                    val action = LoginFragmentDirections.navigateToOtpScreen()
//                    showToast(response.message)
//                    val bundle = bundleOf("token" to response.tokenCode)
//                    findNavController().navigate(R.id.navigate_to_otp_screen, bundle)
                    goToHomeScreen(response.data[0])
                    (activity as? MainActivity)?.hideProgressBar()
                } else {
                    showToast(response.message)
                    (activity as? MainActivity)?.hideProgressBar()
                }
            }
        })
        loginViewModel.errorMessage.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let { errorMessage ->
                showToast(errorMessage)
            }
        })

        setUpViews()

        return binding.root
    }

    private fun setUpViews() {
        binding.apply {
            pinView.setPinViewEventListener { pinView, fromUser ->
                Log.d(
                    "Basava",
                    "setPinViewEventListener: value -> ${pinView.value} fromUser -> $fromUser"
                )
                otp = pinView.value
                login.isEnabled = otp.length == 4
                if (otp.length == 4) hideKeyboard()
            }

            login.setOnClickListener {
                hideKeyboard()
                (activity as? MainActivity)?.showProgressBar()
                loginViewModel.login(args.token, otpPin = otp)
            }

            cancel.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun goToHomeScreen(user: LoginResponse.User) {
        Log.d("Basava", "PINFragment goToHomeScreen: roleName -> $user.roleName")
        when (user.roleName) {
            "admin" -> {
                val bundle = bundleOf("user_id" to user.userId)
                findNavController().navigate(R.id.navigate_to_admin_home_screen, bundle)
            }
            "ground_owner" -> {
                val bundle = bundleOf("user_id" to user.userId)
                findNavController().navigate(R.id.navigate_to_ground_owner_home_screen, bundle)
            }
            // TODO: 29/08/21 Handle other user types here
            else -> {
                error("No role provided")
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}