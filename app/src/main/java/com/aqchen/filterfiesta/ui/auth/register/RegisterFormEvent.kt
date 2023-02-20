package com.aqchen.filterfiesta.ui.auth.register

sealed class RegisterFormEvent {
    data class EmailChanged(val email: String): RegisterFormEvent()
    data class PasswordChanged(val password: String): RegisterFormEvent()
    data class RepeatedPasswordChanged(val repeatedPassword: String): RegisterFormEvent()
    data class TermsChanged(val isAccepted: Boolean): RegisterFormEvent();

    object Submit: RegisterFormEvent()
}
