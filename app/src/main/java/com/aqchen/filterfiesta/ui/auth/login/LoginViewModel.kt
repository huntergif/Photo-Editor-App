package com.aqchen.filterfiesta.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.aqchen.filterfiesta.domain.use_case.auth.GetCurrentUserUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.LoginWithEmailAndPasswordUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.ValidateEmailUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.ValidatePasswordUseCase
import com.aqchen.filterfiesta.util.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val loginWithEmailAndPassword: LoginWithEmailAndPasswordUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
): ViewModel() {
    // use shared flow for login user flow since we want to emit login result only once
    // https://stackoverflow.com/questions/66162586/the-main-difference-between-sharedflow-and-stateflow
    private val _loginUserFlow = MutableSharedFlow<Resource<FirebaseUser?>?>()
    // public readable shared flow
    val loginUserFlow: SharedFlow<Resource<FirebaseUser?>?> = _loginUserFlow

    // only this view model can mutate the state flow
    private val _loginFormStateFlow = MutableStateFlow(
        LoginFormState()
    )
    // public readable state flow
    val loginFormStateFlow: StateFlow<LoginFormState> = _loginFormStateFlow

    val currentUser: FirebaseUser?
        get() = getCurrentUser()

    init {
        if (getCurrentUser() != null) {
            // shared flow can only emit in a coroutine
            viewModelScope.launch {
                _loginUserFlow.emit(Resource.Success(getCurrentUser()))
            }
        }
    }

    fun onEvent(event: LoginFormEvent) {
        when (event) {
            is LoginFormEvent.EmailChanged -> {
                _loginFormStateFlow.value = _loginFormStateFlow.value.copy(email = event.email)
            }
            is LoginFormEvent.PasswordChanged -> {
                _loginFormStateFlow.value = _loginFormStateFlow.value.copy(password = event.password)
            }
            is LoginFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val emailResult = validateEmail(_loginFormStateFlow.value.email)
        val passwordResult = validatePassword(_loginFormStateFlow.value.password, loginValidation = true)

        val hasError = listOf(
            emailResult,
            passwordResult,
        ).any { !it.successful }

        if (hasError) {
            _loginFormStateFlow.value = _loginFormStateFlow.value.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
            )
        } else {
            // no errors in form
            _loginFormStateFlow.value = _loginFormStateFlow.value.copy(
                emailError = null,
                passwordError = null,
            )
            // set login is loading
            viewModelScope.launch {
                _loginUserFlow.emit(Resource.Loading)
            }
            // launch coroutine to login
            viewModelScope.launch {
                _loginUserFlow.emit(loginWithEmailAndPassword(_loginFormStateFlow.value.email, _loginFormStateFlow.value.password))
            }
        }
    }
}
