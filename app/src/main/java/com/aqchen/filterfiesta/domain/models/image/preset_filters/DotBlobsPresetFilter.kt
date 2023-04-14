package com.aqchen.filterfiesta.domain.models.image.preset_filters

import android.graphics.Bitmap
import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.models.image.FilterMatrices
import com.aqchen.filterfiesta.domain.models.image.ParameterSetting
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

// https://stackoverflow.com/questions/60016168/how-to-implement-a-photoshop-like-effect-oilpaint-effect-in-opencv
class DotBlobsPresetFilter: BaseImageFilter(
    type = "dotBlobs",
    name = "Dot Blobs",
    parameterSettings = listOf(
        ParameterSetting(
            type = "kernelWidth",
            name = "Kernel Width",
            default = 6.0,
            min = 1.0,
            max = 11.0,
        )
    )
) {
    override fun apply(
        source: Bitmap,
        parameters: Map<String, Double>,
        matrices: FilterMatrices
    ): Bitmap? {
        val imageMatrix = matrices.matrix1
        val tempMatrix1 = matrices.matrix2

        val kernelWidth = parameters["kernelWidth"] ?: return null

        Utils.bitmapToMat(source, imageMatrix)

        val kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, Size(kernelWidth, kernelWidth))
        Imgproc.morphologyEx(imageMatrix, tempMatrix1, Imgproc.MORPH_OPEN, kernel)

        Core.normalize(tempMatrix1, tempMatrix1, 20.0, 255.0, Core.NORM_MINMAX)

        val resultBitmap = Bitmap.createBitmap(source.width, source.height, source.config)
        Utils.matToBitmap(tempMatrix1, resultBitmap)

        return resultBitmap
    }
}
