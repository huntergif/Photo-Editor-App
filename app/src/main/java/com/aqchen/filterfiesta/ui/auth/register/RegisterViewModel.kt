package com.aqchen.filterfiesta.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aqchen.filterfiesta.domain.use_case.auth.ValidateEmailUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.ValidatePasswordUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.ValidateRepeatedPasswordUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.ValidateTermsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val validateRepeatedPassword: ValidateRepeatedPasswordUseCase,
    private val validateTerms: ValidateTermsUseCase
): ViewModel() {
    private val _registrationFormStateFlow = MutableStateFlow<RegistrationFormState>(
        RegistrationFormState()
    )

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: RegistrationFormEvent) {
        when (event) {
            is RegistrationFormEvent.EmailChanged -> {
                _registrationFormStateFlow.value = _registrationFormStateFlow.value.copy(email = event.email)
            }
            is RegistrationFormEvent.PasswordChanged -> {
                _registrationFormStateFlow.value = _registrationFormStateFlow.value.copy(password = event.password)
            }
            is RegistrationFormEvent.RepeatedPasswordChanged -> {
                _registrationFormStateFlow.value = _registrationFormStateFlow.value.copy(repeatedPassword = event.repeatedPassword)
            }
            is RegistrationFormEvent.TermsChanged -> {
                _registrationFormStateFlow.value = _registrationFormStateFlow.value.copy(acceptedTerms = event.isAccepted)
            }
            is RegistrationFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val emailResult = validateEmail(_registrationFormStateFlow.value.email)
        val passwordResult = validatePassword(_registrationFormStateFlow.value.password)
        val repeatedPasswordResult = validateRepeatedPassword(
            _registrationFormStateFlow.value.password,
            _registrationFormStateFlow.value.repeatedPassword
        )
        val termsResult = validateTerms(_registrationFormStateFlow.value.acceptedTerms)

        val hasError = listOf(
            emailResult,
            passwordResult,
            repeatedPasswordResult,
            termsResult
        ).any { !it.successful }

        if (hasError) {
            _registrationFormStateFlow.value = _registrationFormStateFlow.value.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                repeatedPasswordError = repeatedPasswordResult.errorMessage,
                acceptedTermsError = termsResult.errorMessage
            )
        } else {
            viewModelScope.launch {
                validationEventChannel.send(ValidationEvent.Success)
            }
        }
    }

    sealed class ValidationEvent {
        object Success: ValidationEvent()
    }

}
