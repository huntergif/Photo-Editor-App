package com.aqchen.filterfiesta.domain.models.image.adjustments

import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.models.image.ParameterSetting

class ContrastAdjustment: BaseImageFilter(
    type = "contrast",
    name = "Contrast",
    parameterSettings = listOf(
        ParameterSetting(
            type = "strength",
            name = "Strength",
            default = 0.0,
            min = -1.0,
            max = 1.0,
        )
    )
) {
    override fun apply(parameters: Map<String, Double>) {
        TODO("Not yet implemented")
    }
}
