package com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images

import android.graphics.Bitmap
import android.net.Uri
import com.aqchen.filterfiesta.domain.models.Filter
import com.aqchen.filterfiesta.util.Resource

data class DisplayedPhotoEditorBitmapProgress(
    val min: Int,
    val max: Int,
    val progress: Int,
)

enum class BitmapType {
    PREVIEW_IMAGE, FILTER_PREVIEW, IMAGE_EXPORT_ORIGINAL_RES
}

data class BaseImageBitmaps(
    val base: Bitmap,
    val scaled: Bitmap,
)

sealed class PhotoEditorImagesEvent {
    data class SetBaseImage(val uri: Uri): PhotoEditorImagesEvent()
    data class SetImageFilters(val filters: List<Filter>): PhotoEditorImagesEvent()
    data class SetFilterPreviewFilters(val filters: List<Filter>): PhotoEditorImagesEvent()
    data class SelectFilter(val filters: List<Filter>, val selectPosition: Int): PhotoEditorImagesEvent()
    data class SetBaseImageBitmap(val bitmaps: BaseImageBitmaps?): PhotoEditorImagesEvent()
    data class SetInternalBitmap(val resource: Resource<Bitmap>, val bitmapType: BitmapType): PhotoEditorImagesEvent()
    object ClearFilterPreviewBitmap: PhotoEditorImagesEvent()
    data class SetDisplayedPhotoEditorBitmap(val bitmapResource: Resource<Bitmap>?, val progress: DisplayedPhotoEditorBitmapProgress? = null): PhotoEditorImagesEvent()
    data class GenerateBitmapUsingFilters(val filters: List<Filter>, val bitmapType: BitmapType): PhotoEditorImagesEvent()
    object Save: PhotoEditorImagesEvent()
    object Cancel: PhotoEditorImagesEvent()
}
