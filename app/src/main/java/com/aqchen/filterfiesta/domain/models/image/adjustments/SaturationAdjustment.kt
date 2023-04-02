package com.aqchen.filterfiesta.domain.models.image.adjustments

import android.graphics.Bitmap
import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.models.image.ParameterSetting

class SaturationAdjustment: BaseImageFilter(
    type = "saturation",
    name = "Saturation",
    parameterSettings = listOf(
        ParameterSetting(
            type = "strength",
            name = "Strength",
            default = 0.0,
            min = -1.0,
            max = 1.0,
        ),
    )
) {
    override fun apply(source: Bitmap, parameters: Map<String, Double>): Bitmap? {
        TODO("Not yet implemented")
    }
}
