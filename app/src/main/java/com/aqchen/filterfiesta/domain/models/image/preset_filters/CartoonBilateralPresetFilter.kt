package com.aqchen.filterfiesta.domain.models.image.preset_filters

import android.graphics.Bitmap
import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.models.image.FilterMatrices
import com.aqchen.filterfiesta.domain.models.image.ParameterSetting
import org.opencv.core.Core
import org.opencv.core.TermCriteria
import org.opencv.imgproc.Imgproc

class CartoonBilateralPresetFilter: BaseImageFilter(
    type = "cartoon Bilateral",
    name = "Cartoon Bilateral",
    parameterSettings = listOf(
        ParameterSetting(
            type = "colors",
            name = "Colors",
            default = 8.0,
            min = 1.0,
            max = 255.0,
        ),
        ParameterSetting(
            type = "bilateralDiameter",
            name = "Bilateral Diameter",
            default = 5.0,
            min = 3.0,
            max = 11.0,
        ),
        ParameterSetting(
            type = "sigmaColor",
            name = "Sigma Color",
            default = 0.07,
            min = 0.0,
            max = 1.0,
        ),
        ParameterSetting(
            type = "Sigma Space",
            name = "Sigma Space",
            default = 0.02,
            min = 0.0,
            max = 0.1,
        ),
        ParameterSetting(
            type = "medianDiameter",
            name = "Median Blur Diameter",
            default = 0.02,
            min = 0.0,
            max = 0.1,
        ),
        ParameterSetting(
            type = "edgeDiameter",
            name = "Edge Diameter",
            default = 0.02,
            min = 0.0,
            max = 0.1,
        ),
    )
) {
    override fun apply(
        source: Bitmap,
        parameters: Map<String, Double>,
        matrices: FilterMatrices
    ): Bitmap? {
        val criteria = TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER, 20, 0.001)
        TODO()
    }
}