package com.aqchen.filterfiesta.ui.photo_editor.filter_groups

import com.aqchen.filterfiesta.domain.models.FilterGroup
import com.aqchen.filterfiesta.domain.util.FilterGroupsOrder
import com.aqchen.filterfiesta.domain.util.OrderType
import com.aqchen.filterfiesta.util.Resource
import kotlinx.coroutines.flow.Flow

// State data class should survive lifecycle changes
data class FilterGroupsState(
    val filterGroups: Flow<List<FilterGroup>>? = null,
    val getFilterGroupsStatus: Resource<Flow<List<FilterGroup>>>? = null,
    val filterGroupsOrder: FilterGroupsOrder = FilterGroupsOrder.Name(OrderType.Descending)
)
