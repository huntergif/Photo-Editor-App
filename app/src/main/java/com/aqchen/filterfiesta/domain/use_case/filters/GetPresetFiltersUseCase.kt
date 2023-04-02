package com.aqchen.filterfiesta.domain.use_case.filters

import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.models.image.adjustments.ContrastAdjustment
import com.aqchen.filterfiesta.domain.models.image.adjustments.SaturationAdjustment

class GetPresetFiltersUseCase {
    operator fun invoke(): List<BaseImageFilter> {
        return listOf(
            ContrastAdjustment(),
            SaturationAdjustment(),
        )
    }
}
