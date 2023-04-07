package com.aqchen.filterfiesta.ui.photo_editor.edit_parameters

import androidx.lifecycle.ViewModel
import com.aqchen.filterfiesta.domain.models.image.ParameterSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class EditParametersViewModel @Inject constructor(): ViewModel() {
    private val _selectedParameterSettingStateFlow = MutableStateFlow<ParameterSetting?>(null)
    val selectedParameterSettingStateFlow: StateFlow<ParameterSetting?> =
        _selectedParameterSettingStateFlow

    fun onEvent(event: EditParametersEvent) {
        when (event) {
            is EditParametersEvent.SelectParameter -> {
                _selectedParameterSettingStateFlow.value = event.parameterSetting
            }
        }
    }
}
