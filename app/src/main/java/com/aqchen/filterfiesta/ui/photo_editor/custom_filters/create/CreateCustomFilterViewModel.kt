package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aqchen.filterfiesta.domain.models.CustomFilter
import com.aqchen.filterfiesta.domain.use_case.auth.GetCurrentUserUseCase
import com.aqchen.filterfiesta.domain.use_case.custom_filters.CreateCustomFilterUseCase
import com.aqchen.filterfiesta.domain.use_case.custom_filters.ValidateCustomFilterNameUseCase
import com.aqchen.filterfiesta.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCustomFilterViewModel @Inject constructor(
    private val validateCustomFilterNameUseCase: ValidateCustomFilterNameUseCase,
    private val createCustomFilterUseCase: CreateCustomFilterUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): ViewModel() {
    private val _createCustomFilterFlow = MutableSharedFlow<Resource<Unit>?>()
    // public readable shared flow
    val createCustomFilterFlow: SharedFlow<Resource<Unit>?> = _createCustomFilterFlow

    // only this view model can mutate the state flow
    private val _createCustomFilterFormStateFlow = MutableStateFlow(
        CreateCustomFilterFormState()
    )
    // public readable state flow
    val createCustomFilterFormStateFlow: StateFlow<CreateCustomFilterFormState> = _createCustomFilterFormStateFlow

    // Handle create custom filter form events from the view
    fun onEvent(event: CreateCustomFilterFormEvent) {
        when (event) {
            is CreateCustomFilterFormEvent.NameChanged -> {
                _createCustomFilterFormStateFlow.value = _createCustomFilterFormStateFlow.value.copy(name = event.name)
            }
            is CreateCustomFilterFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val nameResult = validateCustomFilterNameUseCase(_createCustomFilterFormStateFlow.value.name)

        val hasError = listOf(
            nameResult,
        ).any { !it.successful }

        // Set errors to messages (or null)
        _createCustomFilterFormStateFlow.value = _createCustomFilterFormStateFlow.value.copy(
            nameError = nameResult.errorMessage,
        )

        if (!hasError) {
            _createCustomFilterFormStateFlow.value = _createCustomFilterFormStateFlow.value.copy(
                nameError = null,
            )
            viewModelScope.launch {
                _createCustomFilterFlow.emit(Resource.Loading)

                val currentUser = getCurrentUserUseCase()
                if (currentUser == null) {
                    _createCustomFilterFlow.emit(Resource.Error("Not logged-in, try re-authenticating"))
                } else {
                    val newCustomFilter = CustomFilter(name = _createCustomFilterFormStateFlow.value.name, userId = currentUser.uid)
                    _createCustomFilterFlow.emit(createCustomFilterUseCase(newCustomFilter))
                }
            }
        }
    }
}
