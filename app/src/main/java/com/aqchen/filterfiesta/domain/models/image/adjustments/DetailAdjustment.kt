package com.aqchen.filterfiesta.domain.models.image.adjustments

import android.graphics.Bitmap
import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.models.image.FilterMatrices
import com.aqchen.filterfiesta.domain.models.image.ParameterSetting
import com.aqchen.filterfiesta.util.bitmapConfigToCvType
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import org.opencv.photo.Photo

class DetailAdjustment: BaseImageFilter(
    type = "detail",
    name = "Details",
    parameterSettings = listOf(
        ParameterSetting(
            type = "sigmaSpace",
            name = "Sigma Space",
            default = 10.0,
            min = 0.0,
            max = 50.0,
        ),
        ParameterSetting(
            type = "sigmaColor",
            name = "Sigma Color",
            default = 1.0,
            min = 0.0,
            max = 1.0,
        ),
    )
) {

    // https://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-5-contrast-adjustment/
    override fun apply(
        source: Bitmap,
        parameters: Map<String, Double>,
        matrices: FilterMatrices
    ): Bitmap? {
        val imageMatrix = matrices.matrix1

        val sigmaSpace = parameters["sigmaSpace"]?.toFloat() ?: return null
        val sigmaColor = parameters["sigmaColor"]?.toFloat() ?: return null

        Utils.bitmapToMat(source, imageMatrix)

        val origSize = imageMatrix.size()

        Imgproc.pyrDown(imageMatrix, imageMatrix)
        Imgproc.cvtColor(imageMatrix, imageMatrix, Imgproc.COLOR_BGRA2RGB)
        Photo.detailEnhance(imageMatrix, imageMatrix, sigmaSpace, sigmaColor)
        Imgproc.cvtColor(imageMatrix, imageMatrix, Imgproc.COLOR_RGB2BGRA)
        Imgproc.pyrUp(imageMatrix, imageMatrix)
        Imgproc.resize(imageMatrix, imageMatrix, origSize)

        val resultBitmap = Bitmap.createBitmap(source.width, source.height, source.config)
        Utils.matToBitmap(imageMatrix, resultBitmap)

        return resultBitmap
    }
}
