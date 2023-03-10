package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.shared_view_models

import com.aqchen.filterfiesta.domain.models.CustomFilter

sealed class ViewCustomFilterEvent {
    data class SelectCustomFilter(val customFilter: CustomFilter): ViewCustomFilterEvent()
}
