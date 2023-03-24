package com.aqchen.filterfiesta.ui.auth.passwordreset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aqchen.filterfiesta.domain.use_case.auth.SendPasswordResetEmailUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.ValidateEmailUseCase
import com.aqchen.filterfiesta.ui.auth.register.RegisterFormEvent
import com.aqchen.filterfiesta.ui.auth.register.RegisterFormState
import com.aqchen.filterfiesta.util.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PasswordResetViewModel  @Inject constructor(
    private val validateEmail: ValidateEmailUseCase,
    private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase
): ViewModel() {
    // use shared flow for login user flow since we want to emit login result only once
    // https://stackoverflow.com/questions/66162586/the-main-difference-between-sharedflow-and-stateflow
    private val _passwordResetUserFlow = MutableSharedFlow<Resource<FirebaseUser?>?>()
    // public readable shared flow
    val passwordResetUserFlow: SharedFlow<Resource<FirebaseUser?>?> = _passwordResetUserFlow

    // only this view model can mutate the state flow
    private val _passwordResetFormStateFlow = MutableStateFlow(
        PasswordResetFormState()
    )
    // public readable state flow
    val passwordResetFormStateFlow: StateFlow<PasswordResetFormState> = _passwordResetFormStateFlow

    // Handle register form events from the view
    fun onEvent(event: PasswordResetFormEvent) {
        when (event) {
            is PasswordResetFormEvent.EmailChanged -> {
                _passwordResetFormStateFlow.value = _passwordResetFormStateFlow.value.copy(email = event.email)
            }
            is PasswordResetFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        //val emailResult = validateEmail(_passwordResetFormStateFlow.value.email)
        viewModelScope.launch {
            // emit that password reset user flow is loading
            _passwordResetUserFlow.emit(Resource.Loading)
            // emit result resource
            _passwordResetUserFlow.emit(
                sendPasswordResetEmailUseCase(_passwordResetFormStateFlow.value.email)
            )
        }
    }
}