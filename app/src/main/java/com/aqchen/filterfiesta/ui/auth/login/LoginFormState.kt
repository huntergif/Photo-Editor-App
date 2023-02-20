package com.aqchen.filterfiesta.ui.auth.login

import com.google.firebase.auth.FirebaseUser

data class LoginFormState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
)
