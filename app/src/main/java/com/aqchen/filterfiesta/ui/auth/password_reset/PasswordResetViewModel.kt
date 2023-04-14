package com.aqchen.filterfiesta.ui.auth.password_reset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aqchen.filterfiesta.domain.use_case.auth.SendPasswordResetEmailUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.ValidateEmailUseCase
import com.aqchen.filterfiesta.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordResetViewModel  @Inject constructor(
    private val validateEmail: ValidateEmailUseCase,
    private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase
): ViewModel() {
    // use shared flow for login user flow since we want to emit login result only once
    // https://stackoverflow.com/questions/66162586/the-main-difference-between-sharedflow-and-stateflow
    private val _passwordResetResultFlow = MutableSharedFlow<Resource<Unit>?>()
    // public readable shared flow
    val passwordResetResultFlow: SharedFlow<Resource<Unit>?> = _passwordResetResultFlow

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
        val emailResult = validateEmail(_passwordResetFormStateFlow.value.email)

        val hasError = listOf(
            emailResult,
        ).any { !it.successful }

        // Set errors to messages (or null)
        _passwordResetFormStateFlow.value = _passwordResetFormStateFlow.value.copy(
            emailError = emailResult.errorMessage,
        )

        if (!hasError) {
            viewModelScope.launch {
                // emit that password reset user flow is loading
                _passwordResetResultFlow.emit(Resource.Loading)

                val task = sendPasswordResetEmailUseCase(_passwordResetFormStateFlow.value.email)
                task.addOnCompleteListener { taskResult ->
                    viewModelScope.launch {
                        if (taskResult.isSuccessful) {
                            _passwordResetResultFlow.emit(Resource.Success(Unit))
                        } else {
                            _passwordResetResultFlow.emit(Resource.Error(errorMessage = task.exception!!.message.toString()))
                        }
                    }
                }
            }
        }
    }
}