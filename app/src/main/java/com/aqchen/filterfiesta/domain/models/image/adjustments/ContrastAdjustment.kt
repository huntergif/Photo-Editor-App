package com.aqchen.filterfiesta.domain.models.image.adjustments

import android.graphics.Bitmap
import android.net.Uri
import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.models.image.ParameterSetting
import com.bumptech.glide.Glide

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
        ),
        ParameterSetting(
            type = "test",
            name = "Test",
            default = 0.0,
            min = 0.0,
            max = 1.0,
        ),
    )
) {
    override fun apply(source: Bitmap, dest: Bitmap, parameters: Map<String, Double>) {

    }
}
