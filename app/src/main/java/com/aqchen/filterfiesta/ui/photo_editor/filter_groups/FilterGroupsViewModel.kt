package com.aqchen.filterfiesta.ui.photo_editor.filter_groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aqchen.filterfiesta.domain.models.FilterGroup
import com.aqchen.filterfiesta.domain.use_case.filter_groups.FilterGroupsUseCases
import com.aqchen.filterfiesta.domain.util.FilterGroupsOrder
import com.aqchen.filterfiesta.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FilterGroupsModel @Inject constructor(
    private val filterGroupsUseCases: FilterGroupsUseCases
) : ViewModel() {
    // only this view model can mutate the state flow
    private val _filterGroupsStateFlow = MutableStateFlow(
        FilterGroupsState()
    )
    // public readable state flow
    val filterGroupsStateFlow: StateFlow<FilterGroupsState> = _filterGroupsStateFlow

    private var lastDeletedFilterGroup: FilterGroup? = null

    fun onEvent(event: FilterGroupsEvent) {
        when (event) {
            is FilterGroupsEvent.Create -> {

            }
            is FilterGroupsEvent.Update -> {

            }
            is FilterGroupsEvent.Delete -> {

                viewModelScope.launch {
                    filterGroupsUseCases.deleteFilterGroupUseCase(event.filterGroup)
                    lastDeletedFilterGroup = event.filterGroup
                }
            }
            is FilterGroupsEvent.Order -> {
                // Do nothing if the current order in the state matches the order in the event
                // Note: we need ::class or else it will compare referential equality (FilterGroupsOrder is not a data class), which is never true
                if (_filterGroupsStateFlow.value.filterGroupsOrder::class == event.filterGroupsOrder::class &&
                        _filterGroupsStateFlow.value.filterGroupsOrder.orderType == event.filterGroupsOrder.orderType) {
                    return
                }
            }
            FilterGroupsEvent.RestoreFilterGroup -> {
                viewModelScope.launch {
                    // if lastDeletedFilterGroup is null, we return from the coroutine
                    filterGroupsUseCases.createFilterGroupUseCase(lastDeletedFilterGroup ?: return@launch)
                    lastDeletedFilterGroup = null
                }
            }
        }
    }

    private suspend fun getFilterGroupsFlow(filterGroupsOrder: FilterGroupsOrder) {
        _filterGroupsStateFlow.value = _filterGroupsStateFlow.value.copy(getFilterGroupsStatus = Resource.Loading)
        when (val res = filterGroupsUseCases.getFilterGroupsUseCase(filterGroupsOrder)) {
            is Resource.Success -> {
                _filterGroupsStateFlow.value = _filterGroupsStateFlow.value.copy(filterGroups = res.data)
                _filterGroupsStateFlow.value = _filterGroupsStateFlow.value.copy(getFilterGroupsStatus = res)
            }
            // handle Resource.Loading and Resource.Error
            else -> {
                _filterGroupsStateFlow.value = _filterGroupsStateFlow.value.copy(getFilterGroupsStatus = res)
            }
        }
    }
}