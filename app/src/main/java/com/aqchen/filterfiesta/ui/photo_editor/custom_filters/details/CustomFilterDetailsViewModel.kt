package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aqchen.filterfiesta.domain.use_case.custom_filters.CustomFiltersUseCases
import com.aqchen.filterfiesta.domain.use_case.custom_filters.GetCustomFilterUseCase
import com.aqchen.filterfiesta.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomFilterDetailsViewModel @Inject constructor(
    private val customFiltersUseCases: CustomFiltersUseCases,
) : ViewModel() {
    // only this view model can mutate the state flow
    private val _customFilterStateFlow = MutableStateFlow(
        CustomFilterDetailsState()
    )
    // public readable state flow
    val customFilterStateFlow: StateFlow<CustomFilterDetailsState> = _customFilterStateFlow

    private val _deleteCustomFilterStateStateFlow = MutableStateFlow<Resource<Unit>?>(null)
    val deleteCustomFilterStateStateFlow: StateFlow<Resource<Unit>?> = _deleteCustomFilterStateStateFlow

    fun onEvent(event: CustomFilterDetailsEvent) {
        when (event) {
            is CustomFilterDetailsEvent.CustomFilterChanged -> {
                _customFilterStateFlow.value = _customFilterStateFlow.value.copy(customFilter = event.customFilter)
            }
            is CustomFilterDetailsEvent.DeleteCustomFilter -> {
                viewModelScope.launch {
                    _deleteCustomFilterStateStateFlow.emit(Resource.Loading)

                    val customFilterToDelete = _customFilterStateFlow.value.customFilter
                    if (customFilterToDelete == null) {
                        _deleteCustomFilterStateStateFlow.emit(Resource.Error("No custom filter to delete"))
                    } else {
                        _deleteCustomFilterStateStateFlow.emit(
                            customFiltersUseCases.deleteCustomFilterUseCase(customFilterToDelete)
                        )
                    }
                }
            }
            is CustomFilterDetailsEvent.ResetState -> {
                resetState()
            }
        }
    }

    private fun resetState() {
        _customFilterStateFlow.value = CustomFilterDetailsState()
        _deleteCustomFilterStateStateFlow.value = null
    }
}
