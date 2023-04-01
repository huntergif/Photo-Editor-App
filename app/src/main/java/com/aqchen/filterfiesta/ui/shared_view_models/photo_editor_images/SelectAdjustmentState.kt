package com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images

import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter

data class SelectAdjustmentState(
    val adjustment: BaseImageFilter,
    val currentParams: Map<String, Double>? = null,
)