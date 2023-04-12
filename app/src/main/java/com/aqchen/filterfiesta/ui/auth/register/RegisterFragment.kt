package com.aqchen.filterfiesta.ui.auth.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.home.HomeViewModel
import com.aqchen.filterfiesta.util.Resource
import com.aqchen.filterfiesta.util.setTextViewWithClickableSpan
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Set enter and return transitions - pairs with transitions in login fragment
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }

        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get ui elements
        val emailTextInputLayout = view.findViewById<TextInputLayout>(R.id.register_email_input_layout)
        val emailTextInput = view.findViewById<TextInputEditText>(R.id.register_email_input)
        val passwordTextInputLayout = view.findViewById<TextInputLayout>(R.id.register_password_input_layout)
        val passwordTextInput = view.findViewById<TextInputEditText>(R.id.register_password_input)
        val repeatedPasswordTextInputLayout = view.findViewById<TextInputLayout>(R.id.register_repeat_password_input_layout)
        val repeatedPasswordTextInput = view.findViewById<TextInputEditText>(R.id.register_repeat_password_input)
        val termsCheckBox = view.findViewById<MaterialCheckBox>(R.id.register_terms_input)
        val submitButton = view.findViewById<MaterialButton>(R.id.register_submit_button)
        val registerPrivacyPolicyText = view.findViewById<TextView>(R.id.register_privacy_policy_text)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel = ViewModelProvider(requireParentFragment())[RegisterViewModel::class.java]
            homeViewModel = ViewModelProvider(requireParentFragment())[HomeViewModel::class.java]

            // relaunch coroutines when the fragment starts or is restarted
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // register user flow coroutine
                launch {
                    viewModel.registerUserFlow.collect {
                        when (it) {
                            is Resource.Success -> {
                                Snackbar.make(view, R.string.register_successful, Snackbar.LENGTH_LONG).show()
                                findNavController().navigate(R.id.action_global_homeFragment)
                            }
                            is Resource.Error -> {
                                // re-enabled button to try again
                                submitButton.isEnabled = true
                                Snackbar.make(view, R.string.register_failed, Snackbar.LENGTH_LONG).show()
                            }
                            Resource.Loading -> {
                                // disable button while loading
                                submitButton.isEnabled = false
                            }
                            // do nothing if Resource is null - no results yet
                            null -> {}
                        }
                    }
                }
                // register form state coroutine
                launch {
                    viewModel.registerFormStateFlow.collect {
                        emailTextInputLayout.error = it.emailError
                        passwordTextInputLayout.error = it.passwordError
                        repeatedPasswordTextInputLayout.error = it.repeatedPasswordError
                        termsCheckBox.error = it.acceptedTermsError
                        termsCheckBox.isErrorShown = it.acceptedTermsError != null
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

        // set input handlers
        emailTextInput.doAfterTextChanged {
                text -> viewModel.onEvent(RegisterFormEvent.EmailChanged(text.toString()))
        }

        passwordTextInput.doAfterTextChanged {
                text -> viewModel.onEvent(RegisterFormEvent.PasswordChanged(text.toString()))
        }

        repeatedPasswordTextInput.doAfterTextChanged {
                text -> viewModel.onEvent(RegisterFormEvent.RepeatedPasswordChanged(text.toString()))
        }

        termsCheckBox.setOnCheckedChangeListener {
                _, isChecked -> viewModel.onEvent(RegisterFormEvent.TermsChanged(isChecked))
        }

        submitButton.setOnClickListener {
            viewModel.onEvent(RegisterFormEvent.Submit)
        }

        setTextViewWithClickableSpan(
            registerPrivacyPolicyText,
            getString(R.string.register_privacy_policy_notice),
            getString(R.string.register_privacy_policy_action_text),
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://filterfiesta-privacy-policy.aqchen.com/"))
                    startActivity(browserIntent)
                }
            }
        )
    }
}