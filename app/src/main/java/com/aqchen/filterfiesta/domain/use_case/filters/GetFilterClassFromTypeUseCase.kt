package com.aqchen.filterfiesta.domain.use_case.filters

import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.models.image.adjustments.ContrastAdjustment
import com.aqchen.filterfiesta.domain.models.image.adjustments.DetailAdjustment
import com.aqchen.filterfiesta.domain.models.image.adjustments.SaturationAdjustment
import com.aqchen.filterfiesta.domain.models.image.preset_filters.ColorPencilPresetFilter
import com.aqchen.filterfiesta.domain.models.image.preset_filters.PencilPresetFilter
import com.aqchen.filterfiesta.domain.models.image.preset_filters.WaterColorPresetFilter

class GetFilterClassFromTypeUseCase {
    operator fun invoke(type: String): BaseImageFilter? {
        var imageFilter: BaseImageFilter? = null
        when (type) {
            "contrast" -> {
                imageFilter = ContrastAdjustment()
            }
            "saturation" -> {
                imageFilter = SaturationAdjustment()
            }
            "detail" -> {
                imageFilter = DetailAdjustment()
            }
            "pencil" -> {
                imageFilter = PencilPresetFilter()
            }
            "colorPencil" -> {
                imageFilter = ColorPencilPresetFilter()
            }
            "watercolor" -> {
                imageFilter = WaterColorPresetFilter()
            }
        }
        return imageFilter
    }
}
