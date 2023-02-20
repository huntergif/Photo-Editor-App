package com.aqchen.filterfiesta.ui.auth.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.util.Resource
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.loginUserFlow.collect {
                        when (it) {
                            is Resource.Success -> {
                                Snackbar.make(view, R.string.login_successful, Snackbar.LENGTH_LONG).show()
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
                        //emailTextInput.setText(it.email)
                        emailTextInputLayout.error = it.emailError
                        //passwordTextInput.setText(it.password)
                        passwordTextInputLayout.error = it.passwordError
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
    }
}
