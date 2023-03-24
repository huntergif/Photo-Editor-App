package com.aqchen.filterfiesta.ui.auth.passwordreset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.auth.register.RegisterFormEvent
import com.aqchen.filterfiesta.ui.auth.register.RegisterViewModel
import com.aqchen.filterfiesta.util.Resource
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.transition.MaterialSharedAxis
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class PasswordResetFragment : Fragment() {
    companion object {
        fun newInstance() = PasswordResetFragment()
    }

    private lateinit var viewModel: PasswordResetViewModel

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

        return inflater.inflate(R.layout.fragment_password_reset, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailTextInput = view.findViewById<TextInputEditText>(R.id.password_reset_email_input)
        val submitButton = view.findViewById<MaterialButton>(R.id.password_reset_submit_button)

//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel = ViewModelProvider(requireActivity())[PasswordResetViewModel::class.java]
//
//            // relaunch coroutines when the fragment starts or is restarted
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                // register user flow coroutine
////                launch {
////                    viewModel.passwordResetUserFlow.collect {
////                        when (it) {
////                            is Resource.Success -> {
////                                Snackbar.make(view, R.string.register_successful, Snackbar.LENGTH_LONG).show()
////                            }
////                            is Resource.Error -> {
////                                // re-enabled button to try again
////                                submitButton.isEnabled = true
////                                Snackbar.make(view, R.string.register_failed, Snackbar.LENGTH_LONG).show()
////                            }
////                            Resource.Loading -> {
////                                // disable button while loading
////                                submitButton.isEnabled = false
////                            }
////                            // do nothing if Resource is null - no results yet
////                            null -> {}
////                        }
////                    }
////                }
//                // register form state coroutine
//                launch {
//                    viewModel.passwordResetFormStateFlow.collect {
//                        emailTextInputLayout.error = it.emailError
//                        passwordTextInputLayout.error = it.passwordError
//                        repeatedPasswordTextInputLayout.error = it.repeatedPasswordError
//                        termsCheckBox.error = it.acceptedTermsError
//                        termsCheckBox.isErrorShown = it.acceptedTermsError != null
//                    }
//                }
//            }
//        }

        // set input handlers
        emailTextInput.doAfterTextChanged {
                text -> viewModel.onEvent(PasswordResetFormEvent.EmailChanged(text.toString()))
        }

        submitButton.setOnClickListener {
            viewModel.onEvent(PasswordResetFormEvent.Submit)
//            val email: String = emailTextInput.text.toString().trim { it <= ' ' }
//            if (email.isEmpty()) {
//                Toast.makeText(
//                    this.activity,
//                    "Please enter email address",
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else {
//                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
//                    .addOnCompleteListener{task ->
//                        if (task.isSuccessful) {
//                        Toast.makeText(
//                            this.activity,
//                            "Email has successfully been sent to rest your password!",
//                            Toast.LENGTH_LONG
//                        ).show()
//                        } else {
//                            Toast.makeText(
//                                this.activity,
//                                task.exception!!.message.toString(),
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                }
//            }
        }
    }
}

