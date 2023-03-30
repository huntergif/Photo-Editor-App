package com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images

import android.net.Uri
import com.aqchen.filterfiesta.domain.models.Filter
import com.aqchen.filterfiesta.domain.models.image.Adjustment
import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter

sealed class PhotoEditorImagesEvent {
    data class SetBaseImage(val uri: Uri): PhotoEditorImagesEvent()
    data class SetPreviewImage(val uri: Uri): PhotoEditorImagesEvent()
    data class SetFilterOrAdjustmentPreviewImage(val uri: Uri): PhotoEditorImagesEvent()
    data class SetImageFilters(val filters: List<Filter>): PhotoEditorImagesEvent()
    data class SelectAdjustment(val adjustment: BaseImageFilter, val currentParams: Map<String, Double>? = null): PhotoEditorImagesEvent()
}
