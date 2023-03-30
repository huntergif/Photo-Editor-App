package com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images

import androidx.lifecycle.ViewModel
import com.aqchen.filterfiesta.domain.models.Filter
import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.models.image.Image
import com.aqchen.filterfiesta.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PhotoEditorImagesViewModel @Inject constructor(): ViewModel() {
    private val _baseImageStateFlow = MutableStateFlow<Image?>(null);
    val baseImageStateFlow: StateFlow<Image?> = _baseImageStateFlow;

    private val _previewImageStateFlow = MutableStateFlow<Resource<Image>?>(null);
    val previewImageStateFlow: StateFlow<Resource<Image>?> = _previewImageStateFlow;

    private val _imageFiltersStateFlow = MutableStateFlow<List<Filter>>(emptyList())
    val imageFiltersStateFlow: StateFlow<List<Filter>> = _imageFiltersStateFlow;

    private val _selectedAdjustmentStateFlow = MutableStateFlow<SelectAdjustmentState?>(null)
    val selectedAdjustmentStateFlow: StateFlow<SelectAdjustmentState?> = _selectedAdjustmentStateFlow

    fun onEvent(event: PhotoEditorImagesEvent) {
        when (event) {
            is PhotoEditorImagesEvent.SetBaseImage -> {
                _baseImageStateFlow.value = Image(imageUri = event.uri)
            }
            is PhotoEditorImagesEvent.SetFilterOrAdjustmentPreviewImage -> TODO()
            is PhotoEditorImagesEvent.SetPreviewImage -> TODO()
            is PhotoEditorImagesEvent.SetImageFilters -> {
                _imageFiltersStateFlow.value = event.filters
            }
            is PhotoEditorImagesEvent.SelectAdjustment -> {
                _selectedAdjustmentStateFlow.value = SelectAdjustmentState(event.adjustment, event.currentParams)
            }
        }
    }
}
