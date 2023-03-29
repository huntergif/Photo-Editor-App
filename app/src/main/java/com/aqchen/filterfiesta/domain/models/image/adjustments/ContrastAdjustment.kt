package com.aqchen.filterfiesta.domain.models.image.adjustments

import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.models.image.ParameterSettings

class ContrastAdjustment: BaseImageFilter(
    type = "contrast",
    name = "Contrast",
    parameterSettings = mapOf(
        "strength" to ParameterSettings(
            name = "Strength",
            min = -1.0,
            max = 1.0,
            default = 0.0,
        )
    )
) {
    override fun apply(parameters: Map<String, Double>) {
        TODO("Not yet implemented")
    }
}
