package com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images

import com.aqchen.filterfiesta.domain.models.Filter
import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter

data class SelectFilterState(
    val filter: BaseImageFilter,
    val filters: List<Filter>,
    val selectPosition: Int,
)
