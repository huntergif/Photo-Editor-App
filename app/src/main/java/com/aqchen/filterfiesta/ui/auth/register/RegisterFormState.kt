package com.aqchen.filterfiesta.ui.auth.register

data class RegisterFormState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: String? = null,
    val acceptedTerms: Boolean = false,
    val acceptedTermsError: String? = null,
)
