package com.aqchen.filterfiesta.ui.auth.passwordreset

sealed class PasswordResetFormEvent {
    data class EmailChanged(val email: String): PasswordResetFormEvent()
    object Submit: PasswordResetFormEvent()
}
