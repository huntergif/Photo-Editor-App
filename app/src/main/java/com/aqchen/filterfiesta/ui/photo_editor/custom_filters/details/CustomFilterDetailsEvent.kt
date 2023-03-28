package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.details

import com.aqchen.filterfiesta.domain.models.CustomFilter

sealed class CustomFilterDetailsEvent {
    data class CustomFilterChanged(val customFilter: CustomFilter): CustomFilterDetailsEvent()
    //data class LoadCustomFilter(val customFilterId: String?) : CustomFiltersDetailsEvent()
    object DeleteCustomFilter : CustomFilterDetailsEvent()
    object ResetState : CustomFilterDetailsEvent() // Use after successful deletion
}
