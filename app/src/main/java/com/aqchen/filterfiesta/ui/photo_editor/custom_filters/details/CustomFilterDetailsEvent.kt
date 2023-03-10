package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.details

import com.aqchen.filterfiesta.domain.models.CustomFilter

sealed class CustomFiltersDetailsEvent {
    data class CustomFilterChanged(val customFilter: CustomFilter): CustomFiltersDetailsEvent()
    //data class LoadCustomFilter(val customFilterId: String?) : CustomFiltersDetailsEvent()
    object DeleteCustomFilter : CustomFiltersDetailsEvent()
    object ResetState : CustomFiltersDetailsEvent() // Use after successful deletion
}
