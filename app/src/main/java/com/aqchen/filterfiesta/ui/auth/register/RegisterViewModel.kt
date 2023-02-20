package com.aqchen.filterfiesta.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aqchen.filterfiesta.domain.use_case.auth.CreateUserWithEmailAndPasswordUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.GetCurrentUserUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.ValidateEmailUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.ValidatePasswordUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.ValidateRepeatedPasswordUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.ValidateTermsUseCase
import com.aqchen.filterfiesta.ui.auth.login.LoginFormState
import com.aqchen.filterfiesta.util.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val validateRepeatedPassword: ValidateRepeatedPasswordUseCase,
    private val validateTerms: ValidateTermsUseCase,
    private val createUserWithEmailAndPasswordUseCase: CreateUserWithEmailAndPasswordUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
): ViewModel() {
    // use shared flow for login user flow since we want to emit login result only once
    // https://stackoverflow.com/questions/66162586/the-main-difference-between-sharedflow-and-stateflow
    private val _registerUserFlow = MutableSharedFlow<Resource<FirebaseUser?>?>()
    // public readable shared flow
    val registerUserFlow: SharedFlow<Resource<FirebaseUser?>?> = _registerUserFlow

    // only this view model can mutate the state flow
    private val _registerFormStateFlow = MutableStateFlow(
        RegisterFormState()
    )
    // public readable state flow
    val registerFormStateFlow: StateFlow<RegisterFormState> = _registerFormStateFlow

    val currentUser: FirebaseUser?
        get() = getCurrentUser()

    // Check if user is already logged-in
    init {
        if (getCurrentUser() != null) {
            // shared flow can only emit in a coroutine
            viewModelScope.launch {
                _registerUserFlow.emit(Resource.Success(getCurrentUser()))
            }
        }
    }

    // Handle register form events from the view
    fun onEvent(event: RegisterFormEvent) {
        when (event) {
            is RegisterFormEvent.EmailChanged -> {
                _registerFormStateFlow.value = _registerFormStateFlow.value.copy(email = event.email)
            }
            is RegisterFormEvent.PasswordChanged -> {
                _registerFormStateFlow.value = _registerFormStateFlow.value.copy(password = event.password)
            }
            is RegisterFormEvent.RepeatedPasswordChanged -> {
                _registerFormStateFlow.value = _registerFormStateFlow.value.copy(repeatedPassword = event.repeatedPassword)
            }
            is RegisterFormEvent.TermsChanged -> {
                _registerFormStateFlow.value = _registerFormStateFlow.value.copy(acceptedTerms = event.isAccepted)
            }
            is RegisterFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val emailResult = validateEmail(_registerFormStateFlow.value.email)
        val passwordResult = validatePassword(_registerFormStateFlow.value.password)
        val repeatedPasswordResult = validateRepeatedPassword(
            _registerFormStateFlow.value.password,
            _registerFormStateFlow.value.repeatedPassword
        )
        val termsResult = validateTerms(_registerFormStateFlow.value.acceptedTerms)

        val hasError = listOf(
            emailResult,
            passwordResult,
            repeatedPasswordResult,
            termsResult
        ).any { !it.successful }

        // Set errors to messages (or null)
        _registerFormStateFlow.value = _registerFormStateFlow.value.copy(
            emailError = emailResult.errorMessage,
            passwordError = passwordResult.errorMessage,
            repeatedPasswordError = repeatedPasswordResult.errorMessage,
            acceptedTermsError = termsResult.errorMessage
        )

        if (!hasError) {
            // launch coroutine to register
            viewModelScope.launch {
                // emit that register user flow is loading
                _registerUserFlow.emit(Resource.Loading)
                // emit result resource
                _registerUserFlow.emit(
                    createUserWithEmailAndPasswordUseCase(
                        _registerFormStateFlow.value.email,
                        _registerFormStateFlow.value.password)
                )
            }
        }
    }
}
