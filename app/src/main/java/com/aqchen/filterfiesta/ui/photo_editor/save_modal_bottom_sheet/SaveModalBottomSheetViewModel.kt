package com.aqchen.filterfiesta.ui.photo_editor.save_modal_bottom_sheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aqchen.filterfiesta.domain.use_case.custom_filters.UpdateCustomFilterUseCase
import com.aqchen.filterfiesta.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveModalBottomSheetViewModel @Inject constructor(
    private val updateCustomFilterUseCase: UpdateCustomFilterUseCase
): ViewModel() {
    private val _exportStateFlow = MutableStateFlow<Resource<Unit>?>(null)
    val exportStateFlow: StateFlow<Resource<Unit>?> = _exportStateFlow

    private val _saveCustomFilterStateFlow = MutableStateFlow<Resource<Unit>?>(null)
    val saveCustomFilterStateFlow: StateFlow<Resource<Unit>?> = _saveCustomFilterStateFlow

    private val _exportTypeStateFlow = MutableStateFlow<ExportType?>(null)
    val exportTypeStateFlow: StateFlow<ExportType?> = _exportTypeStateFlow

    fun onEvent(event: SaveModalBottomSheetEvent) {
        when (event) {
            is SaveModalBottomSheetEvent.SaveAsCustomFilter -> {
                val newCustomFilter = event.customFilter.copy(filters = event.newFilters)
                viewModelScope.launch {
                    _saveCustomFilterStateFlow.value = Resource.Loading
                    _saveCustomFilterStateFlow.emit(updateCustomFilterUseCase(newCustomFilter))
                }
            }
            is SaveModalBottomSheetEvent.SetExportState -> {
                _exportStateFlow.value = event.state
            }
            is SaveModalBottomSheetEvent.SetExportType -> {
                _exportTypeStateFlow.value = event.exportType
            }
        }
    }
}
