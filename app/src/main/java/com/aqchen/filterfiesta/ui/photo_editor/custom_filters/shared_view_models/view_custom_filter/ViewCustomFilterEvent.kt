package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.shared_view_models.view_custom_filter

import com.aqchen.filterfiesta.domain.models.CustomFilter

/* ViewCustomFilterEvent is used for "selecting" a single custom filter for other fragments to use so they
 * don't need to re-fetch the custom filter. Used in the details and edit_details fragments
 */

sealed class ViewCustomFilterEvent {
    data class SelectCustomFilter(val customFilter: CustomFilter): ViewCustomFilterEvent()
}
