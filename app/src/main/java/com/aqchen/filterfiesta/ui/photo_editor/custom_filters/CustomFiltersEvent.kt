package com.aqchen.filterfiesta.ui.photo_editor.custom_filters

import com.aqchen.filterfiesta.domain.models.CustomFilter
import com.aqchen.filterfiesta.domain.util.FilterGroupsOrder

sealed class CustomFiltersEvent {
    data class Order(val filterGroupsOrder: FilterGroupsOrder): CustomFiltersEvent()
    data class Create(val customFilter: CustomFilter): CustomFiltersEvent()
    data class Update(val customFilter: CustomFilter): CustomFiltersEvent()
    object RestoreCustomFilter: CustomFiltersEvent()

    object LoadCustomFilters: CustomFiltersEvent()
}
