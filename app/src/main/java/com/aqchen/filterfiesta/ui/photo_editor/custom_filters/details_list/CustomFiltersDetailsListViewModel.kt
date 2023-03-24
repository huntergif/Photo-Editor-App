package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.details_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aqchen.filterfiesta.domain.use_case.custom_filters.GetCustomFiltersUseCase
import com.aqchen.filterfiesta.domain.util.FilterGroupsOrder
import com.aqchen.filterfiesta.ui.photo_editor.custom_filters.CustomFiltersState
import com.aqchen.filterfiesta.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomFiltersDetailsListViewModel @Inject constructor(
    private val getCustomFiltersUseCase: GetCustomFiltersUseCase
) : ViewModel() {
    // only this view model can mutate the state flow
    private val _filterGroupsStateFlow = MutableStateFlow(
        CustomFiltersDetailsListState()
    )
    // public readable state flow
    val customFiltersStateFlow: StateFlow<CustomFiltersDetailsListState> = _filterGroupsStateFlow

    fun onEvent(event: CustomFiltersDetailsListEvent) {
        when (event) {
            is CustomFiltersDetailsListEvent.Order -> {
                // Do nothing if the current order in the state matches the order in the event
                // Note: we need ::class or else it will compare referential equality (FilterGroupsOrder is not a data class), which is never true
                if (_filterGroupsStateFlow.value.customFiltersOrder::class == event.filterGroupsOrder::class &&
                    _filterGroupsStateFlow.value.customFiltersOrder.orderType == event.filterGroupsOrder.orderType) {
                    return
                }
            }
            is CustomFiltersDetailsListEvent.LoadCustomFilters -> {
                viewModelScope.launch {
                    getCustomFiltersFlow(customFiltersStateFlow.value.customFiltersOrder)
                }
            }
        }
    }

    private suspend fun getCustomFiltersFlow(filterGroupsOrder: FilterGroupsOrder) {
        _filterGroupsStateFlow.value = _filterGroupsStateFlow.value.copy(getCustomFiltersStatus = Resource.Loading)
        when (val res = getCustomFiltersUseCase(filterGroupsOrder)) {
            is Resource.Success -> {
                _filterGroupsStateFlow.value = _filterGroupsStateFlow.value.copy(customFilters = res.data)
                _filterGroupsStateFlow.value = _filterGroupsStateFlow.value.copy(getCustomFiltersStatus = Resource.Success(Unit))
            }
            is Resource.Loading -> {
                _filterGroupsStateFlow.value = _filterGroupsStateFlow.value.copy(getCustomFiltersStatus = Resource.Loading)
            }
            is Resource.Error -> {
                _filterGroupsStateFlow.value = _filterGroupsStateFlow.value.copy(getCustomFiltersStatus = Resource.Error(res.errorMessage))
            }
        }
    }
}
