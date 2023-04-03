package com.aqchen.filterfiesta.domain.models.image.preset_filters

import android.graphics.Bitmap
import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.models.image.ParameterSetting
import com.aqchen.filterfiesta.util.bitmapConfigToCvType
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import org.opencv.photo.Photo

class WaterColorPresetFilter: BaseImageFilter(
    type = "watercolor",
    name = "Watercolor",
    parameterSettings = listOf(
        ParameterSetting(
            type = "sigmaSpace",
            name = "Sigma Space",
            default = 60.0,
            min = 0.0,
            max = 200.0,
        ),
        ParameterSetting(
            type = "sigmaColor",
            name = "Sigma Color",
            default = 0.45,
            min = 0.0,
            max = 1.0,
        ),
    )
) {

    // https://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-5-contrast-adjustment/
    override fun apply(source: Bitmap, parameters: Map<String, Double>): Bitmap? {
        val cvType = bitmapConfigToCvType(source.config)
        val imageMatrix = Mat(source.height, source.width, cvType)

        val sigmaSpace = parameters["sigmaSpace"]?.toFloat() ?: return null
        val sigmaColor = parameters["sigmaColor"]?.toFloat() ?: return null

        Utils.bitmapToMat(source, imageMatrix)

        val origSize = imageMatrix.size()

        Imgproc.pyrDown(imageMatrix, imageMatrix)
        Imgproc.cvtColor(imageMatrix, imageMatrix, Imgproc.COLOR_BGRA2RGB)
        Photo.stylization(imageMatrix, imageMatrix, sigmaSpace, sigmaColor)
        Imgproc.cvtColor(imageMatrix, imageMatrix, Imgproc.COLOR_RGB2BGRA)
        Imgproc.pyrUp(imageMatrix, imageMatrix)
        Imgproc.resize(imageMatrix, imageMatrix, origSize)

        val resultBitmap = Bitmap.createBitmap(source.width, source.height, source.config)
        Utils.matToBitmap(imageMatrix, resultBitmap)

        return resultBitmap
    }
}
