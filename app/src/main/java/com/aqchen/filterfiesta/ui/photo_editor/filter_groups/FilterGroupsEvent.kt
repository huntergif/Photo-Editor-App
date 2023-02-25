package com.aqchen.filterfiesta.ui.photo_editor.filter_groups

import com.aqchen.filterfiesta.domain.models.FilterGroup
import com.aqchen.filterfiesta.domain.util.FilterGroupsOrder

sealed class FilterGroupsEvent {
    data class Order(val filterGroupsOrder: FilterGroupsOrder): FilterGroupsEvent()
    data class Create(val filterGroup: FilterGroup): FilterGroupsEvent()
    data class Update(val filterGroup: FilterGroup): FilterGroupsEvent()
    data class Delete(val filterGroup: FilterGroup): FilterGroupsEvent()
    object RestoreFilterGroup: FilterGroupsEvent()
}
