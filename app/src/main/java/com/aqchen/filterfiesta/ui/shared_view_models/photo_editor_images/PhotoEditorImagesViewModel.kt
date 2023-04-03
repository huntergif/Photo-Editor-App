package com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images

import android.graphics.Bitmap
import android.util.Log
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PhotoEditorImagesViewModel @Inject constructor(
    private val getFilterClassFromTypeUseCase: GetFilterClassFromTypeUseCase,
    private val generateImageUseCase: GenerateImageUseCase,
): ViewModel() {
    // state flow for the base (initial selected) image
    private val _baseImageStateFlow = MutableStateFlow<Image?>(null)
    val baseImageStateFlow: StateFlow<Image?> = _baseImageStateFlow

    var lastProcessedImage: Image? = null

    // state flow for the image filters (the filter list which would be applied to the final saved image)
    private val _imageFiltersStateFlow = MutableStateFlow<List<Filter>>(emptyList())
    val imageFiltersStateFlow: StateFlow<List<Filter>> = _imageFiltersStateFlow

    // state flow for the selected filter state (for when the user chooses a filter to add or edit)
    private val _selectedFilterStateFlow = MutableStateFlow<SelectFilterState?>(null)
    val selectedFilterStateFlow: StateFlow<SelectFilterState?> = _selectedFilterStateFlow

    // state flow for the bitmap loaded from the base image uri
    private val _baseImageBitmapStateFlow = MutableStateFlow<Bitmap?>(null)
    val baseImageBitmapStateFlow: StateFlow<Bitmap?> = _baseImageBitmapStateFlow

    // state flow for the preview image bitmap (the final image that would be saved)
    private val _previewImageBitmapStateFlow = MutableStateFlow<Resource<Bitmap>?>(null)
    val previewImageBitmapStateFlow: StateFlow<Resource<Bitmap>?> = _previewImageBitmapStateFlow

    // state flow for the filter preview (the preview for when the user is adding or editing a filter, not confirmed as to be saved)
    private val _filterPreviewBitmapStateFlow = MutableStateFlow<Resource<Bitmap>?>(null)
    val filterPreviewBitmapStateFlow: StateFlow<Resource<Bitmap>?> = _filterPreviewBitmapStateFlow

    // state flow for the bitmap to be displayed on the photo editor. Usually either the preview image or filter preview bitmap
    private val _displayPhotoEditorBitmapStateFlow = MutableStateFlow<Resource<Bitmap>?>(null)
    val displayPhotoEditorBitmapStateFlow: StateFlow<Resource<Bitmap>?> = _displayPhotoEditorBitmapStateFlow

    private val _saveEventStateFlow = MutableSharedFlow<List<Int>>()
    val saveEventStateFlow: SharedFlow<List<Int>> = _saveEventStateFlow

    private val _cancelEventStateFlow = MutableSharedFlow<List<Int>>()
    val cancelEventStateFlow: SharedFlow<List<Int>> = _cancelEventStateFlow

    private var generateImageJob: Job? = null
    private var generateImageType: BitmapType? = null

    fun onEvent(event: PhotoEditorImagesEvent) {
        when (event) {
            is PhotoEditorImagesEvent.SetBaseImage -> {
                _baseImageStateFlow.value = Image(imageUri = event.uri)
            }
            is PhotoEditorImagesEvent.SetImageFilters -> {
                _imageFiltersStateFlow.value = event.filters
            }
            is PhotoEditorImagesEvent.SelectFilter -> {
                val filter = getFilterClassFromTypeUseCase(event.filters[event.selectPosition].type)
                if (filter == null) {
                    _selectedFilterStateFlow.value = null
                } else {
                    _selectedFilterStateFlow.value = SelectFilterState(filter = filter, filters = event.filters, selectPosition = event.selectPosition)
                }
            }
            is PhotoEditorImagesEvent.SetBaseImageBitmap -> {
                _baseImageBitmapStateFlow.value = event.bitmap
            }
            is PhotoEditorImagesEvent.SetInternalBitmap -> {
                when (event.bitmapType) {
                    BitmapType.PREVIEW_IMAGE -> {
                        _previewImageBitmapStateFlow.value = Resource.Success(event.bitmap)
                    }
                    BitmapType.FILTER_PREVIEW -> {
                        _filterPreviewBitmapStateFlow.value = Resource.Success(event.bitmap)
                    }
                }
            }
            is PhotoEditorImagesEvent.SetDisplayedPhotoEditorBitmap -> {
                _displayPhotoEditorBitmapStateFlow.value = event.bitmapResource
            }
            is PhotoEditorImagesEvent.GenerateBitmapUsingFilters -> {
                generateImage(event.filters, event.bitmapType)
            }
            is PhotoEditorImagesEvent.Save -> {
                Log.d("ViewModel", "RECIEVED SAVE")
                viewModelScope.launch {
                    _saveEventStateFlow.emit(emptyList())

                }
            }
            is PhotoEditorImagesEvent.Cancel -> {
                viewModelScope.launch {
                    _cancelEventStateFlow.emit(emptyList())
                }
            }
        }
    }

    private fun generateImage(filters: List<Filter>, bitmapType: BitmapType) {
        // change any "loading" state flows to null and cancel any currently running image generation job
        when (generateImageType) {
            BitmapType.PREVIEW_IMAGE -> {
                _previewImageBitmapStateFlow.value = null
            }
            BitmapType.FILTER_PREVIEW -> {
                _filterPreviewBitmapStateFlow.value = null
            }
            null -> {}
        }
        generateImageJob?.cancel()

        // determine which bitmap (preview image for filter preview we're changing
        generateImageType = bitmapType
        val stateFlow = when (bitmapType) {
            BitmapType.PREVIEW_IMAGE -> {
                _previewImageBitmapStateFlow
            }
            BitmapType.FILTER_PREVIEW -> {
                _filterPreviewBitmapStateFlow
            }
        }

        // get base image bitmap
        val baseBitmap = baseImageBitmapStateFlow.value
        if (baseBitmap == null) {
            stateFlow.value = Resource.Error("No base image bitmap set")
            return
        }

        // start generate image job
        generateImageJob = viewModelScope.launch {
            stateFlow.emit(Resource.Loading)
            // debounce generate image requests by 200ms
            delay(200)
            val newBitmap = generateImageUseCase(baseBitmap, filters)
            if (newBitmap == null) {
                stateFlow.emit(Resource.Error("Failed to generate image"))
            } else {
                stateFlow.emit(Resource.Success(newBitmap))
            }
        }
    }
}
