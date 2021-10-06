package com.bookmygame.ui.login

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bookmygame.DrawerLocker
import com.bookmygame.MainActivity
import com.bookmygame.R
import com.bookmygame.databinding.FragmentLoginBinding
import com.bookmygame.util.hideKeyboard
import java.util.regex.Pattern

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var loginType: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("Basava", "LoginFragment onAttach")
        requireActivity().window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(context, R.color.colorPrimary)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Log.d("Basava", "LoginFragment onCreate")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        Log.d("Basava", "LoginFragment onCreateOptionsMenu")
        (activity as DrawerLocker).setDrawerLocked(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("Basava", "LoginFragment onCreateView")
//        (activity as DrawerLocker).setDrawerLocked(true)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.loginTypeResponse.observe(viewLifecycleOwner, { event ->
            Log.d("Basava", "LoginFragment loginTypeResponse.observe")
            event.getContentIfNotHandled()?.let { response ->
                if (response.error.not() && response.tokenCode.isNullOrEmpty().not()) {
                    Log.d("Basava", "Going to OTP screen!")
                    showToast(response.message)
                    val bundle = bundleOf("token" to response.tokenCode)
                    if (loginType == "otp") {
                        findNavController().navigate(R.id.navigate_to_otp_screen, bundle)
                    } else if (loginType == "pin") {
                        findNavController().navigate(R.id.navigate_to_pin_screen, bundle)
                    }
                } else {
                    showToast(response.message)
//                    binding.layoutMobileNumber.error = response.message
                }
                (activity as? MainActivity)?.hideProgressBar()
            }
        })

        loginViewModel.errorMessage.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let { errorMessage ->
                showToast(errorMessage)
                (activity as? MainActivity)?.hideProgressBar()
            }
        })

        setUpViews()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
//        (requireActivity() as MainActivity).supportActionBar?.hide()
    }

    override fun onDestroyView() {
        Log.d("Basava", "LoginFragment onDestroyView")
//        (activity as DrawerLocker).setDrawerLocked(false)
        _binding = null
        loginViewModel.loginTypeResponse.removeObservers(viewLifecycleOwner)
        loginViewModel.errorMessage.removeObservers(viewLifecycleOwner)
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Basava", "LoginFragment onViewCreated")
    }

    private fun setUpViews() {
        binding.apply {
            edittextMobileNumber.addTextChangedListener {
//                if (it.isNullOrEmpty().not()) layoutMobileNumber.error = ""
            }

            getOtp.setOnClickListener {
                loginType = "otp"
                startLogin()
            }

            haveAPin.apply {
                paintFlags = haveAPin.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                setOnClickListener {
                    loginType = "pin"
                    startLogin()
                }
            }
        }
    }

    private fun startLogin() {
        val mobileNumber = binding.edittextMobileNumber.text.toString().trim()
        if (mobileNumber.isEmpty().not()) {
            val pattern =
                "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*\$"
            val r = Pattern.compile(pattern)
            val matcher = r.matcher(mobileNumber)
            if (matcher.find()) {
                hideKeyboard()
                (activity as? MainActivity)?.showProgressBar()
                loginViewModel.loginType(mobileNumber = mobileNumber, loginType = loginType)
            } else {
                showToast("Please Enter valid Phone Number")
            }
        } else {
            showToast("Please Enter Phone Number")
        }
    }

    override fun onDestroy() {
        Log.d("Basava", "LoginFragment onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
//        (requireActivity() as MainActivity).supportActionBar?.show()
    }

    private fun showToast(text: String?) {
        if (text.isNullOrEmpty()) return
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}