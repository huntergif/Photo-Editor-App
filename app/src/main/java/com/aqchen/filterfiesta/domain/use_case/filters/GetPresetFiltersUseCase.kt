package com.aqchen.filterfiesta.domain.use_case.filters

import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.models.image.preset_filters.ColorPencilPresetFilter
import com.aqchen.filterfiesta.domain.models.image.preset_filters.DotBlobsPresetFilter
import com.aqchen.filterfiesta.domain.models.image.preset_filters.PencilPresetFilter
import com.aqchen.filterfiesta.domain.models.image.preset_filters.VignettePresetFilter
import com.aqchen.filterfiesta.domain.models.image.preset_filters.WaterColorPresetFilter

class GetPresetFiltersUseCase {
    operator fun invoke(): List<BaseImageFilter> {
        return listOf(
            PencilPresetFilter(),
            ColorPencilPresetFilter(),
            WaterColorPresetFilter(),
            DotBlobsPresetFilter(),
        )
    }
}
