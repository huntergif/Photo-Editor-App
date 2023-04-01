package com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images

import android.graphics.Bitmap
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aqchen.filterfiesta.domain.models.Filter
import com.aqchen.filterfiesta.domain.models.image.Image
import com.aqchen.filterfiesta.domain.use_case.filters.GenerateImageUseCase
import com.aqchen.filterfiesta.domain.use_case.filters.GetFilterClassFromTypeUseCase
import com.aqchen.filterfiesta.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Thread.State
import javax.inject.Inject

@HiltViewModel
class PhotoEditorImagesViewModel @Inject constructor(
    private val getFilterClassFromTypeUseCase: GetFilterClassFromTypeUseCase,
    private val generateImageUseCase: GenerateImageUseCase,
): ViewModel() {
    private val _baseImageStateFlow = MutableStateFlow<Image?>(null)
    val baseImageStateFlow: StateFlow<Image?> = _baseImageStateFlow

    private val _previewImageStateFlow = MutableStateFlow<Resource<Image>?>(null)
    val previewImageStateFlow: StateFlow<Resource<Image>?> = _previewImageStateFlow

    private val _imageFiltersStateFlow = MutableStateFlow<List<Filter>>(emptyList())
    val imageFiltersStateFlow: StateFlow<List<Filter>> = _imageFiltersStateFlow

    private val _filterPreviewFiltersStateFlow = MutableStateFlow<List<Filter>>(emptyList())
    val filterPreviewFiltersStateFlow: StateFlow<List<Filter>> = _filterPreviewFiltersStateFlow

    private val _selectedFilterStateFlow = MutableStateFlow<SelectFilterState?>(null)
    val selectedFilterStateFlow: StateFlow<SelectFilterState?> = _selectedFilterStateFlow

    private val _previewFilterStateFlow = MutableStateFlow<Filter?>(null)
    val previewFilterStateFlow: StateFlow<Filter?> = _previewFilterStateFlow

    private val _previewFilterPositionStateFlow = MutableStateFlow<Int?>(null)
    val previewFilterPositionStateFlow: StateFlow<Int?> = _previewFilterPositionStateFlow

    private var generateImageJob: Job? = null

    lateinit var previewImageFile: File
    lateinit var filterPreviewImageFile: File

    private val _previewImageBitmapStateFlow = MutableStateFlow<Bitmap?>(null)
    val previewImageBitmapStateFlow: StateFlow<Bitmap?> = _previewImageBitmapStateFlow

    private val _filterPreviewBitmapStateFlow = MutableStateFlow<Bitmap?>(null)
    val filterPreviewBitmapStateFlow: StateFlow<Bitmap?> = _filterPreviewBitmapStateFlow

    fun onEvent(event: PhotoEditorImagesEvent) {
        when (event) {
            is PhotoEditorImagesEvent.SetBaseImage -> {
                _baseImageStateFlow.value = Image(imageUri = event.uri)
                // TODO: TRY SETTING BITMAP HERE
            }
            is PhotoEditorImagesEvent.SetFilterOrAdjustmentPreviewImage -> TODO()
            is PhotoEditorImagesEvent.SetPreviewImage -> TODO()
            is PhotoEditorImagesEvent.SetImageFilters -> {
                _imageFiltersStateFlow.value = event.filters
            }
            is PhotoEditorImagesEvent.SetPreviewFilter -> {
                _previewFilterStateFlow.value = event.filter
                _filterPreviewFiltersStateFlow.value = imageFiltersStateFlow.value + event.filter
            }
            is PhotoEditorImagesEvent.SelectFilter -> {
                val filter = getFilterClassFromTypeUseCase(event.filterValue.type)
                if (filter == null) {
                    _selectedFilterStateFlow.value = null
                } else {
                    _selectedFilterStateFlow.value = SelectFilterState(filter = filter, filterValue = event.filterValue, filterListPosition = event.filterListPosition)
                }
            }
            is PhotoEditorImagesEvent.ShowPreview -> {
                val index = previewFilterPositionStateFlow.value ?: imageFiltersStateFlow.value.size
                val previewFilter = previewFilterStateFlow.value
                if (previewFilter != null) {
                    val mutableFilterList = imageFiltersStateFlow.value.toMutableList()
                    mutableFilterList.add(index, previewFilter)
                    generateImage(mutableFilterList.toList())
                }
            }
        }
    }

    fun generateImage(filters: List<Filter>) {
        generateImageJob?.cancel()
        generateImageJob = viewModelScope.launch {
            delay(500)
        }
    }
}
