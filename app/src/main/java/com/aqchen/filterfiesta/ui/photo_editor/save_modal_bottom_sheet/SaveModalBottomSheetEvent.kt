package com.aqchen.filterfiesta.ui.photo_editor.save_modal_bottom_sheet

import com.aqchen.filterfiesta.domain.models.CustomFilter
import com.aqchen.filterfiesta.domain.models.Filter
import com.aqchen.filterfiesta.util.Resource

sealed class SaveModalBottomSheetEvent {
    data class SaveAsCustomFilter(val customFilter: CustomFilter, val newFilters: List<Filter>): SaveModalBottomSheetEvent()
    data class SetSaveState(val saveState: Resource<Unit>?): SaveModalBottomSheetEvent()
}
