package com.aqchen.filterfiesta.ui.photo_editor.custom_filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aqchen.filterfiesta.domain.models.FilterGroup
import com.aqchen.filterfiesta.domain.use_case.filter_groups.FilterGroupsUseCases
import com.aqchen.filterfiesta.domain.util.FilterGroupsOrder
import com.aqchen.filterfiesta.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class FilterGroupsViewModel @Inject constructor(
    private val filterGroupsUseCases: FilterGroupsUseCases
) : ViewModel() {
    // only this view model can mutate the state flow
    private val _filterGroupsStateFlow = MutableStateFlow(
        FilterGroupsState()
    )
    // public readable state flow
    val filterGroupsStateFlow: StateFlow<FilterGroupsState> = _filterGroupsStateFlow

    private var lastDeletedFilterGroup: FilterGroup? = null

    fun onEvent(event: CustomFiltersEvent) {
        when (event) {
            is CustomFiltersEvent.Create -> {

            }
            is CustomFiltersEvent.Update -> {

            }
            is CustomFiltersEvent.Delete -> {

                viewModelScope.launch {
                    filterGroupsUseCases.deleteFilterGroupUseCase(event.filterGroup)
                    lastDeletedFilterGroup = event.filterGroup
                }
            }
            is CustomFiltersEvent.Order -> {
                // Do nothing if the current order in the state matches the order in the event
                // Note: we need ::class or else it will compare referential equality (FilterGroupsOrder is not a data class), which is never true
                if (_filterGroupsStateFlow.value.filterGroupsOrder::class == event.filterGroupsOrder::class &&
                    _filterGroupsStateFlow.value.filterGroupsOrder.orderType == event.filterGroupsOrder.orderType) {
                    return
                }
            }
            is CustomFiltersEvent.RestoreFilterGroup -> {
                viewModelScope.launch {
                    // if lastDeletedFilterGroup is null, we return from the coroutine
                    filterGroupsUseCases.createFilterGroupUseCase(lastDeletedFilterGroup ?: return@launch)
                    lastDeletedFilterGroup = null
                }
            }
            is CustomFiltersEvent.LoadCustomFilters -> {
                viewModelScope.launch {
                    getFilterGroupsFlow(filterGroupsStateFlow.value.filterGroupsOrder)
                }
            }
        }
    }

    private suspend fun getFilterGroupsFlow(filterGroupsOrder: FilterGroupsOrder) {
        _filterGroupsStateFlow.value = _filterGroupsStateFlow.value.copy(getFilterGroupsStatus = Resource.Loading)
        when (val res = filterGroupsUseCases.getFilterGroupsUseCase(filterGroupsOrder)) {
            is Resource.Success -> {
                _filterGroupsStateFlow.value = _filterGroupsStateFlow.value.copy(filterGroups = res.data)
                _filterGroupsStateFlow.value = _filterGroupsStateFlow.value.copy(getFilterGroupsStatus = Resource.Success(Unit))
            }
            is Resource.Loading -> {
                _filterGroupsStateFlow.value = _filterGroupsStateFlow.value.copy(getFilterGroupsStatus = Resource.Loading)
            }
            is Resource.Error -> {
                _filterGroupsStateFlow.value = _filterGroupsStateFlow.value.copy(getFilterGroupsStatus = Resource.Error(res.errorMessage))
            }
        }
    }
}