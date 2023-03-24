package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.details_list

import com.aqchen.filterfiesta.domain.util.FilterGroupsOrder

sealed class CustomFiltersDetailsListEvent {
    data class Order(val filterGroupsOrder: FilterGroupsOrder): CustomFiltersDetailsListEvent()
    object LoadCustomFilters: CustomFiltersDetailsListEvent()
}
