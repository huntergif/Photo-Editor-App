package com.aqchen.filterfiesta.ui.auth.password_reset

sealed class PasswordResetFormEvent {
    data class EmailChanged(val email: String): PasswordResetFormEvent()
    object Submit: PasswordResetFormEvent()
}
