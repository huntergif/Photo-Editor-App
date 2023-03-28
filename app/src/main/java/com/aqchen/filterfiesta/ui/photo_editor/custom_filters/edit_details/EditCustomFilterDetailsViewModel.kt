package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.edit_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aqchen.filterfiesta.domain.use_case.auth.GetCurrentUserUseCase
import com.aqchen.filterfiesta.domain.use_case.custom_filters.UpdateCustomFilterUseCase
import com.aqchen.filterfiesta.domain.use_case.custom_filters.ValidateCustomFilterNameUseCase
import com.aqchen.filterfiesta.domain.use_case.custom_filters.ValidateCustomFilterUpdatableUseCase
import com.aqchen.filterfiesta.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCustomFilterDetailsViewModel @Inject constructor(
    private val validateCustomFilterNameUseCase: ValidateCustomFilterNameUseCase,
    private val updateCustomFilterUseCase: UpdateCustomFilterUseCase,
    private val validateCustomFilterUpdatableUseCase: ValidateCustomFilterUpdatableUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private val _editCustomFilterFlow = MutableSharedFlow<Resource<Unit>?>()
    // public readable shared flow
    val editCustomFilterFlow: SharedFlow<Resource<Unit>?> = _editCustomFilterFlow

    // only this view model can mutate the state flow
    private val _editCustomFilterFormStateFlow = MutableStateFlow(
        EditCustomFilterDetailsFormState()
    )
    // public readable state flow
    val editCustomFilterFormStateFlow: StateFlow<EditCustomFilterDetailsFormState> = _editCustomFilterFormStateFlow

    // Handle create custom filter form events from the view
    fun onEvent(event: EditCustomFilterDetailsFormEvent) {
        when (event) {
            is EditCustomFilterDetailsFormEvent.CustomFilterChanged -> {
                _editCustomFilterFormStateFlow.value = _editCustomFilterFormStateFlow.value.copy(customFilter = event.customFilter)
            }
            is EditCustomFilterDetailsFormEvent.NameChanged -> {
                _editCustomFilterFormStateFlow.value = _editCustomFilterFormStateFlow.value.copy(name = event.name)
            }
            is EditCustomFilterDetailsFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val formState = _editCustomFilterFormStateFlow.value

        val customFilterResult = validateCustomFilterUpdatableUseCase(formState.customFilter)
        val nameResult = validateCustomFilterNameUseCase(formState.name)

        val hasError = listOf(
            customFilterResult,
            nameResult,
        ).any { !it.successful }

        // Set errors to messages (or null)
        _editCustomFilterFormStateFlow.value = _editCustomFilterFormStateFlow.value.copy(
            customFilterError = customFilterResult.errorMessage,
            nameError = nameResult.errorMessage,
        )

        if (!hasError) {
            // no errors in form
            _editCustomFilterFormStateFlow.value = _editCustomFilterFormStateFlow.value.copy(
                customFilterError = null,
                nameError = null,
            )
            viewModelScope.launch {
                _editCustomFilterFlow.emit(Resource.Loading)

                val currentUser = getCurrentUserUseCase()
                if (currentUser == null) {
                    _editCustomFilterFlow.emit(Resource.Error("Not logged-in, try re-authenticating"))
                } else if (formState.customFilter == null) {
                    _editCustomFilterFlow.emit(Resource.Error("Invalid custom filter to edit"))
                } else {
                    _editCustomFilterFlow.emit(updateCustomFilterUseCase(formState.customFilter.copy(name = formState.name)))
                }
            }
        }
    }
}
