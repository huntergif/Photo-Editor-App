package com.aqchen.filterfiesta.ui.photo_editor.preset_filters

import androidx.lifecycle.ViewModel
import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.use_case.filters.GetPresetFiltersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PresetFiltersViewModel @Inject constructor(
    private val getPresetFiltersUseCase: GetPresetFiltersUseCase
) : ViewModel() {
    fun getPresetFiltersList(): List<BaseImageFilter> {
        return getPresetFiltersUseCase()
    }
}
