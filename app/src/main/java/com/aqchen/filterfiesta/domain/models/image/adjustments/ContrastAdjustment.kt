package com.aqchen.filterfiesta.domain.models.image.adjustments

import android.graphics.Bitmap
import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.models.image.ParameterSetting
import com.aqchen.filterfiesta.util.bitmapConfigToCvType
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat

class ContrastAdjustment: BaseImageFilter(
    type = "contrast",
    name = "Contrast",
    parameterSettings = listOf(
        ParameterSetting(
            type = "strength",
            name = "Strength",
            default = 0.0,
            min = -127.0,
            max = 127.0,
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

    // https://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-5-contrast-adjustment/
    override fun apply(source: Bitmap, parameters: Map<String, Double>): Bitmap? {
        val cvType = bitmapConfigToCvType(source.config)
        val imageMatrix = Mat(source.height, source.width, cvType)
        val tempMatrix = Mat(source.height, source.width, cvType) // holds result

        Utils.bitmapToMat(source, imageMatrix)

        val strength = parameters["strength"] ?: return null
        val f = 131 * (strength + 127) / (127 * (131 - strength)) // f is also alpha
        val gamma = 127 * (1 - f)
        // https://docs.opencv.org/4.x/d2/de8/group__core__array.html#gafafb2513349db3bcff51f54ee5592a19
        Core.addWeighted(imageMatrix, f, imageMatrix, 0.0, gamma, tempMatrix)
        // note w/h order different from OpenCV. We also don't want to copy the data from the source, only the dimens and config to reduce un-needed work
        val resultBitmap = Bitmap.createBitmap(source.width, source.height, source.config)
        Utils.matToBitmap(tempMatrix, resultBitmap)
        return resultBitmap
    }
}
