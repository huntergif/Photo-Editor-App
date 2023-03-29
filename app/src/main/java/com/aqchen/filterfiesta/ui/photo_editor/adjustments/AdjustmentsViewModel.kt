package com.aqchen.filterfiesta.ui.photo_editor.adjustments

import androidx.lifecycle.ViewModel
import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.use_case.filters.GetAdjustmentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdjustmentsViewModel @Inject constructor(
    private val getAdjustmentsUseCase: GetAdjustmentsUseCase
): ViewModel() {
    fun onEvent(event: AdjustmentsEvent) {

    }

    fun getAdjustmentsList(): List<BaseImageFilter> {
        return getAdjustmentsUseCase()
    }
}