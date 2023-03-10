package com.aqchen.filterfiesta.ui.photo_editor.custom_filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aqchen.filterfiesta.domain.models.CustomFilter
import com.aqchen.filterfiesta.domain.use_case.custom_filters.CustomFiltersUseCases
import com.aqchen.filterfiesta.domain.util.FilterGroupsOrder
import com.aqchen.filterfiesta.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class CustomFiltersViewModel @Inject constructor(
    private val customFiltersUseCases: CustomFiltersUseCases
) : ViewModel() {
    // only this view model can mutate the state flow
    private val _filterGroupsStateFlow = MutableStateFlow(
        CustomFiltersState()
    )
    // public readable state flow
    val customFiltersStateFlow: StateFlow<CustomFiltersState> = _filterGroupsStateFlow

    private var lastDeletedCustomFilter: CustomFilter? = null

    fun onEvent(event: CustomFiltersEvent) {
        when (event) {
            is CustomFiltersEvent.Create -> {
                viewModelScope.launch {
                    customFiltersUseCases.createFilterGroupUseCase(event.customFilter)
                }
            }
            is CustomFiltersEvent.Update -> {
                viewModelScope.launch {
                    customFiltersUseCases.updateFilterGroupUseCase(event.customFilter)
                }
            }
            is CustomFiltersEvent.Order -> {
                // Do nothing if the current order in the state matches the order in the event
                // Note: we need ::class or else it will compare referential equality (FilterGroupsOrder is not a data class), which is never true
                if (_filterGroupsStateFlow.value.customFiltersOrder::class == event.filterGroupsOrder::class &&
                    _filterGroupsStateFlow.value.customFiltersOrder.orderType == event.filterGroupsOrder.orderType) {
                    return
                }
            }
            is CustomFiltersEvent.RestoreCustomFilter -> {
                viewModelScope.launch {
                    // if lastDeletedFilterGroup is null, we return from the coroutine
                    customFiltersUseCases.createFilterGroupUseCase(lastDeletedCustomFilter ?: return@launch)
                    lastDeletedCustomFilter = null
                }
            }
            is CustomFiltersEvent.LoadCustomFilters -> {
                viewModelScope.launch {
                    getCustomFiltersFlow(customFiltersStateFlow.value.customFiltersOrder)
                }
            }
        }
    }

    private suspend fun getCustomFiltersFlow(filterGroupsOrder: FilterGroupsOrder) {
        _filterGroupsStateFlow.value = _filterGroupsStateFlow.value.copy(getCustomFiltersStatus = Resource.Loading)
        when (val res = customFiltersUseCases.getCustomFiltersUseCase(filterGroupsOrder)) {
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