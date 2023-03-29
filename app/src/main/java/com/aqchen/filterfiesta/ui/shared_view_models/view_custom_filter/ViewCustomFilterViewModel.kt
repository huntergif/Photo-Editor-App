package com.aqchen.filterfiesta.ui.shared_view_models.view_custom_filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aqchen.filterfiesta.domain.models.CustomFilter
import com.aqchen.filterfiesta.domain.use_case.custom_filters.GetCustomFilterUseCase
import com.aqchen.filterfiesta.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Thread.State
import javax.inject.Inject

@HiltViewModel
class ViewCustomFilterViewModel @Inject constructor(
    private val getCustomFilterUseCase: GetCustomFilterUseCase
) : ViewModel() {
    // only this view model can mutate the state flow
    private val _viewCustomFilterStateFlow = MutableStateFlow<CustomFilter?>(null)
    // public readable state flow
    val viewCustomFilterStateFlow: StateFlow<CustomFilter?> = _viewCustomFilterStateFlow

    private val _viewCustomFilterStatusStateFlow = MutableStateFlow<Resource<Unit>?>(null)
    val viewCustomFilterStatusStateFlow: StateFlow<Resource<Unit>?> = _viewCustomFilterStatusStateFlow

    fun onEvent(event: ViewCustomFilterEvent) {
        when (event) {
            is ViewCustomFilterEvent.SelectCustomFilter -> {
                viewModelScope.launch {
                    _viewCustomFilterStatusStateFlow.emit(Resource.Loading)
                    val res = getCustomFilterUseCase(event.customFilter.id)
                    when (res) {
                        is Resource.Error -> {
                            _viewCustomFilterStatusStateFlow.emit(Resource.Error(res.errorMessage))
                        }
                        is Resource.Success -> {
                            _viewCustomFilterStatusStateFlow.emit(Resource.Success(Unit))
                            res.data.collect {
                                _viewCustomFilterStateFlow.value = it
                            }
                        }
                        is Resource.Loading -> {}
                    }
                }
            }
        }
    }
}
