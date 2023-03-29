package com.aqchen.filterfiesta.domain.models.image.adjustments

import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.models.image.ParameterSettings

class SaturationAdjustment: BaseImageFilter(
    type = "saturation",
    name = "Saturation",
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
