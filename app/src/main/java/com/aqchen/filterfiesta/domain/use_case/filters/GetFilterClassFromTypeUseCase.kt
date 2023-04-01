package com.aqchen.filterfiesta.domain.use_case.filters

import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.models.image.adjustments.ContrastAdjustment
import com.aqchen.filterfiesta.domain.models.image.adjustments.SaturationAdjustment

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
        }
        return imageFilter
    }
}
