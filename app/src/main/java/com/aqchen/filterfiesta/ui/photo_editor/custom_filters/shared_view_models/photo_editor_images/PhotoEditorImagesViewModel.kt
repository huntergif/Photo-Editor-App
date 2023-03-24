package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.shared_view_models.photo_editor_images

import androidx.lifecycle.ViewModel
import com.aqchen.filterfiesta.domain.models.image.Image
import com.aqchen.filterfiesta.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PhotoEditorImagesViewModel @Inject constructor(): ViewModel() {
    private val _baseImageStateFlow = MutableStateFlow<Resource<Image>?>(null);
    val baseImageStateFlow: StateFlow<Resource<Image>?> = _baseImageStateFlow;

    private val _previewImageStateFlow = MutableStateFlow<Resource<Image>?>(null);
    val previewImageStateFlow: StateFlow<Resource<Image>?> = _previewImageStateFlow;

    fun onEvent(event: PhotoEditorImagesEvent) {
        when (event) {
            is PhotoEditorImagesEvent.SetBaseImage -> TODO()
            is PhotoEditorImagesEvent.SetFilterOrAdjustmentPreviewImage -> TODO()
            is PhotoEditorImagesEvent.SetPreviewImage -> TODO()
        }
    }
}
