package com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images

import android.net.Uri

sealed class PhotoEditorImagesEvent {

    data class SetBaseImage(val uri: Uri): PhotoEditorImagesEvent()
    data class SetPreviewImage(val uri: Uri): PhotoEditorImagesEvent()
    data class SetFilterOrAdjustmentPreviewImage(val uri: Uri): PhotoEditorImagesEvent()
}
