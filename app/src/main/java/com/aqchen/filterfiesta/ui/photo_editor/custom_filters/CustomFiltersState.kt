package com.aqchen.filterfiesta.ui.photo_editor.custom_filters

import com.aqchen.filterfiesta.domain.models.CustomFilter
import com.aqchen.filterfiesta.domain.util.FilterGroupsOrder
import com.aqchen.filterfiesta.domain.util.OrderType
import com.aqchen.filterfiesta.util.Resource
import kotlinx.coroutines.flow.Flow

// State data class should survive lifecycle changes
data class CustomFiltersState(
    val customFilters: Flow<List<CustomFilter>>? = null,
    val getCustomFiltersStatus: Resource<Unit>? = null,
    val customFiltersOrder: FilterGroupsOrder = FilterGroupsOrder.Name(OrderType.Descending)
)
