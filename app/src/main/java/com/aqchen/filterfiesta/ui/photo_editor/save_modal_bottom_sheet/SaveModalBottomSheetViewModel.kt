package com.aqchen.filterfiesta.ui.photo_editor.save_modal_bottom_sheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aqchen.filterfiesta.domain.use_case.custom_filters.UpdateCustomFilterUseCase
import com.aqchen.filterfiesta.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveModalBottomSheetViewModel @Inject constructor(
    private val updateCustomFilterUseCase: UpdateCustomFilterUseCase
): ViewModel() {
    private val _saveStateFlow = MutableStateFlow<Resource<Unit>?>(null)
    val saveStateFlow: StateFlow<Resource<Unit>?> = _saveStateFlow

    fun onEvent(event: SaveModalBottomSheetEvent) {
        when (event) {
            is SaveModalBottomSheetEvent.SaveAsCustomFilter -> {
                val newCustomFilter = event.customFilter.copy(filters = event.newFilters)
                viewModelScope.launch {
                    _saveStateFlow.value = Resource.Loading
                    _saveStateFlow.emit(updateCustomFilterUseCase(newCustomFilter))
                }
            }
            is SaveModalBottomSheetEvent.SetSaveState -> {
                _saveStateFlow.value = event.saveState
            }
        }
    }
}
