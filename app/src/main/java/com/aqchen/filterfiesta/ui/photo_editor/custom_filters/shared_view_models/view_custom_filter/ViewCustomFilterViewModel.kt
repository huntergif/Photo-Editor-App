package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.shared_view_models

import androidx.lifecycle.ViewModel
import com.aqchen.filterfiesta.domain.models.CustomFilter
import com.aqchen.filterfiesta.ui.photo_editor.custom_filters.shared_view_models.ViewCustomFilterEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ViewCustomFilterViewModel @Inject constructor() : ViewModel() {
    // only this view model can mutate the state flow
    private val _viewCustomFilterStateFlow = MutableStateFlow<CustomFilter?>(null)
    // public readable state flow
    val viewCustomFilterStateFlow: StateFlow<CustomFilter?> = _viewCustomFilterStateFlow

    fun onEvent(event: ViewCustomFilterEvent) {
        when (event) {
            is ViewCustomFilterEvent.SelectCustomFilter -> {
                _viewCustomFilterStateFlow.value = event.customFilter
            }
        }
    }
}
