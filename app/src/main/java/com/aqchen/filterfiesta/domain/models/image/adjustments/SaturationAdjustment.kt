package com.aqchen.filterfiesta.domain.models.image.adjustments

import android.graphics.Bitmap
import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.models.image.ParameterSetting
import com.aqchen.filterfiesta.util.bitmapConfigToCvType
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class SaturationAdjustment: BaseImageFilter(
    type = "saturation",
    name = "Saturation",
    parameterSettings = listOf(
        ParameterSetting(
            type = "strength",
            name = "Strength",
            default = 1.0,
            min = 0.0,
            max = 2.0,
        ),
    )
) {
    override fun apply(source: Bitmap, parameters: Map<String, Double>): Bitmap? {
        val cvType = bitmapConfigToCvType(source.config)
        val imageMatrix = Mat(source.height, source.width, cvType)
        val tempMatrix = Mat(source.height, source.width, cvType)

        Utils.bitmapToMat(source, imageMatrix)

        val strength = parameters["strength"] ?: return null

        Imgproc.cvtColor(imageMatrix, tempMatrix, Imgproc.COLOR_BGRA2GRAY)
        Imgproc.cvtColor(tempMatrix, tempMatrix, Imgproc.COLOR_GRAY2RGB)
        Imgproc.cvtColor(imageMatrix, imageMatrix, Imgproc.COLOR_BGRA2RGB)

        Core.addWeighted(imageMatrix, strength, tempMatrix, 1 - strength, 0.0, imageMatrix)
        Imgproc.cvtColor(imageMatrix, imageMatrix, Imgproc.COLOR_RGB2BGRA)

        val resultBitmap = Bitmap.createBitmap(source.width, source.height, source.config)
        Utils.matToBitmap(imageMatrix, resultBitmap)
        return resultBitmap
    }
}
