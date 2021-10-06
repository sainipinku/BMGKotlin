package com.bookmygame.ui.login

import android.os.Bundle
import android.os.CountDownTimer
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
import com.bookmygame.databinding.FragmentOtpBinding
import com.bookmygame.network.model.LoginResponse
import com.bookmygame.util.SPConstants
import com.bookmygame.util.hideKeyboard

class OTPFragment : Fragment() {

    private var _binding: FragmentOtpBinding? = null

    private val binding get() = _binding!!

    private val args by navArgs<OTPFragmentArgs>()

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var otp: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Log.d("Basava", "OTPFragment onCreate")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        Log.d("Basava", "OTPFragment onCreateOptionsMenu")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        _binding = FragmentOtpBinding.inflate(inflater, container, false)

        Log.d("Basava", "OTPFragment onCreateView: token -> ${args.token}")

        loginViewModel.loginResponse.observe(viewLifecycleOwner, { event ->
            Log.d("Basava", "OTPFragment loginResponse.observe")
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
//                    binding.layoutOtp.error = "Invalid OTP"
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

    override fun onDestroyView() {
        Log.d("Basava", "OTPFragment onDestroyView")
//        (activity as DrawerLocker).setDrawerLocked(false)
        _binding = null
        loginViewModel.loginResponse.removeObservers(viewLifecycleOwner)
        loginViewModel.errorMessage.removeObservers(viewLifecycleOwner)
        hideKeyboard()
        super.onDestroyView()
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

//            otp.addTextChangedListener {
//                if (it.isNullOrEmpty().not()) layoutOtp.error = ""
//            }

            resendOtp.setOnClickListener {
                Log.d("Basava", "resendOtp clicked")
                // TODO: 12/09/21 Call resend OTP API
            }

            val timer = object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    resendOtp.isEnabled = true
                    time.text = "00:${millisUntilFinished / 1000}"
                }

                override fun onFinish() {

                }
            }
            timer.start()

            login.setOnClickListener {
//                val otp = otp.text.toString().trim()
//                if (otp.isEmpty().not()) {
//                    binding.layoutOtp.error = ""
//                    hideKeyboard()
//                    (activity as? MainActivity)?.showProgressBar()
//                    loginViewModel.login(args.token, otpPin = otp)
//                } else {
//                    showToast("Please enter the OTP")
////                    layoutOtp.error = "Please Enter the OTP"
//                }

                hideKeyboard()
                (activity as? MainActivity)?.showProgressBar()
                loginViewModel.login(args.token, otpPin = otp)
            }
        }
    }

    private fun goToHomeScreen(user: LoginResponse.User) {
        (activity as MainActivity).apply {
            saveValue(SPConstants.ROLE, user.roleName)
            saveValue(SPConstants.USED_ID, user.userId)
        }

        Log.d("Basava", "OTPFragment goToHomeScreen: roleName -> ${user.roleName}")
        when (user.roleName) {
            "admin" -> {
                val bundle = bundleOf("user_id" to user.userId)
                findNavController().navigate(R.id.navigate_to_admin_home_screen, bundle)
            }
            "ground_owner" -> {
                val bundle = bundleOf("user_id" to user.userId)
                findNavController().navigate(R.id.navigate_to_ground_owner_home_screen, bundle)
            }
            "user" -> {
                val bundle = bundleOf("user_id" to user.userId)
                findNavController().navigate(R.id.navigate_to_user_home_screen, bundle)
            }
            else -> {
                error("No role provided")
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}