package com.aqchen.filterfiesta.ui.auth.passwordreset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aqchen.filterfiesta.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.transition.MaterialSharedAxis
import com.google.firebase.auth.FirebaseAuth


class PasswordResetFragment : Fragment() {
    companion object {
        fun newInstance() = PasswordResetFragment()
    }

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

        submitButton.setOnClickListener {
            val email: String = emailTextInput.text.toString().trim { it <= ' ' }
            if (email.isEmpty()) {
                Toast.makeText(
                    this.activity,
                    "Please enter email address",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{task ->
                        if (task.isSuccessful){
                        Toast.makeText(
                            this.activity,
                            "Email has successfully been sent to rest your password!",
                            Toast.LENGTH_LONG
                        ).show()
                        } else {
                            Toast.makeText(
                                this.activity,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
            }
        }
    }
}

