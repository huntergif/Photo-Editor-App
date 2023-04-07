package com.aqchen.filterfiesta.ui.photo_editor.edit_parameters.pager

import androidx.lifecycle.ViewModel
import com.aqchen.filterfiesta.domain.models.image.ParameterSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ParameterPagerViewModel @Inject constructor() : ViewModel() {
    private val _currentPositionFlow = MutableStateFlow(0)
    val currentPositionFlow: StateFlow<Int> = _currentPositionFlow

    private val _selectedPositionFlow = MutableStateFlow(0)
    val selectedPositionFlow: StateFlow<Int> = _selectedPositionFlow

    var previousSelectedPosition: Int? = null

    fun onEvent(event: ParameterPagerEvent) {
        when (event) {
            is ParameterPagerEvent.SelectFocusedParameterPage -> {
                previousSelectedPosition = _selectedPositionFlow.value
                _selectedPositionFlow.value = _currentPositionFlow.value
            }
            is ParameterPagerEvent.FocusParameterPageViewHolder -> {
                _currentPositionFlow.value = event.position
            }
        }
    }
}
