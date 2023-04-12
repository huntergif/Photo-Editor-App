package com.aqchen.filterfiesta.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.home.HomeViewModel
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesEvent
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesViewModel
import com.aqchen.filterfiesta.util.Resource
import com.aqchen.filterfiesta.util.setTextViewWithClickableSpan
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailTextInputLayout = view.findViewById<TextInputLayout>(R.id.login_email_input_layout)
        val emailTextInput = view.findViewById<TextInputEditText>(R.id.login_email_input)
        val passwordTextInputLayout = view.findViewById<TextInputLayout>(R.id.login_password_input_layout)
        val passwordTextInput = view.findViewById<TextInputEditText>(R.id.login_password_input)
        val submitButton = view.findViewById<MaterialButton>(R.id.login_submit_button)
        val signUpText = view.findViewById<TextView>(R.id.login_signup_text)
        val forgotPasswordText = view.findViewById<TextView>(R.id.forgot_password_text)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel = ViewModelProvider(requireParentFragment())[LoginViewModel::class.java]
            homeViewModel = ViewModelProvider(requireParentFragment())[HomeViewModel::class.java]

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.loginUserFlow.collect {
                        when (it) {
                            is Resource.Success -> {
                                // listen to authStateFlow to handle success
                            }
                            is Resource.Error -> {
                                submitButton.isEnabled = true
                                Snackbar.make(view, R.string.login_failed, Snackbar.LENGTH_LONG).show()
                            }
                            Resource.Loading -> {
                                submitButton.isEnabled = false
                            }
                            null -> {}
                        }
                    }
                }
                launch {
                    viewModel.loginFormStateFlow.collect {
                        emailTextInputLayout.error = it.emailError
                        passwordTextInputLayout.error = it.passwordError
                    }
                }
                // listen to auth state to determine when to navigate
                launch {
                    homeViewModel.authStateFlow.collect {
                        if (it != null) {
                            findNavController().navigate(R.id.action_global_homeFragment)
                            Snackbar.make(view, R.string.login_successful, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        emailTextInput.doAfterTextChanged {
                text -> viewModel.onEvent(LoginFormEvent.EmailChanged(text.toString()))
        }

        passwordTextInput.doAfterTextChanged {
                text -> viewModel.onEvent(LoginFormEvent.PasswordChanged(text.toString()))
        }

        submitButton.setOnClickListener {
            viewModel.onEvent(LoginFormEvent.Submit)
        }

        //Clickable text for Sign Up
        setTextViewWithClickableSpan(
            signUpText,
            getString(R.string.login_no_account_question),
            getString(R.string.login_signup_action_text),
            object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    // Resolve ?attr/colorPrimary attribute at runtime
                    val color = TypedValue().let {
                        requireContext().theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, it, true)
                        requireContext().getColor(it.resourceId)
                    }
                    // set clickable text color
                    ds.color = color
                    // remove underline from clickable text
                    ds.isUnderlineText = false
                }

                override fun onClick(widget: View) {
                    // Set exit and renter transitions to the register fragment
                    // note that we also need to set transitions in the register fragment onCreateView
                    exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
                        duration = resources.getInteger(R.integer.motion_duration_large).toLong()
                    }
                    reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
                        duration = resources.getInteger(R.integer.motion_duration_large).toLong()
                    }
                    // navigate to register fragment
                    findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
                }
            }
        )

        //Clickable text for Forgot Password
        setTextViewWithClickableSpan(
            forgotPasswordText,
            getString(R.string.login_forgot_password_question),
            getString(R.string.login_password_action_text),
            object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    // Resolve ?attr/colorPrimary attribute at runtime
                    val color = TypedValue().let {
                        requireContext().theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, it, true)
                        requireContext().getColor(it.resourceId)
                    }
                    // set clickable text color
                    ds.color = color
                    // remove underline from clickable text
                    ds.isUnderlineText = false
                }

                override fun onClick(widget: View) {
                    // Set exit and renter transitions to the register fragment
                    // note that we also need to set transitions in the register fragment onCreateView
                    exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
                        duration = resources.getInteger(R.integer.motion_duration_large).toLong()
                    }
                    reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
                        duration = resources.getInteger(R.integer.motion_duration_large).toLong()
                    }
                    // navigate to register fragment
                    findNavController().navigate(R.id.action_loginFragment_to_passwordResetFragment)
                }
            }
        )
    }
}