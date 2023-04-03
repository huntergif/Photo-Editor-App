package com.aqchen.filterfiesta.domain.use_case.filters

import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.models.image.preset_filters.ColorPencilPresetFilter
import com.aqchen.filterfiesta.domain.models.image.preset_filters.PencilPresetFilter

class GetPresetFiltersUseCase {
    operator fun invoke(): List<BaseImageFilter> {
        return listOf(
            PencilPresetFilter(),
            ColorPencilPresetFilter(),
        )
    }
}
