package com.aqchen.filterfiesta.domain.models.image.preset_filters

import android.graphics.Bitmap
import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.models.image.FilterMatrices
import com.aqchen.filterfiesta.domain.models.image.ParameterSetting
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.at
import org.opencv.imgproc.Imgproc
import kotlin.math.max
import kotlin.math.sqrt

class VignettePresetFilter: BaseImageFilter(
    type = "vignette",
    name = "Vignette",
    parameterSettings = listOf(
        ParameterSetting(
            type = "sigma",
            name = "Sigma",
            default = 10.0,
            min = 0.0,
            max = 150.0,
        ),
    )
) {
    override fun apply(
        source: Bitmap,
        parameters: Map<String, Double>,
        matrices: FilterMatrices
    ): Bitmap? {
        val imageMatrix = matrices.matrix1
        val tempMatrix1 = matrices.matrix2
        val tempMatrix2 = matrices.matrix3

        val sigma = parameters["sigma"] ?: return null

        Utils.bitmapToMat(source, imageMatrix)

        Imgproc.cvtColor(imageMatrix, imageMatrix, Imgproc.COLOR_BGRA2RGB)
        Imgproc.cvtColor(tempMatrix1, tempMatrix1, Imgproc.COLOR_BGRA2RGB)
        Imgproc.cvtColor(tempMatrix2, tempMatrix2, Imgproc.COLOR_BGRA2RGB)

        val centerX = imageMatrix.cols() / 2
        val centerY = imageMatrix.rows() / 2

        val rows = imageMatrix.rows()
        val cols = imageMatrix.cols()

        val a = Imgproc.getGaussianKernel(2 * cols, sigma).rowRange(cols - centerX, 2 * cols - centerX)
        val b = Imgproc.getGaussianKernel(2 * rows, sigma).rowRange(rows - centerY, 2 * rows - centerY)

        val c = b.matMul(a.t())
        Core.normalize(c, tempMatrix1, 1.0, 0.0, Core.NORM_INF)
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                // TODO
            }
        }


        Imgproc.cvtColor(tempMatrix1, tempMatrix1, Imgproc.COLOR_RGB2BGRA)

        val resultBitmap = Bitmap.createBitmap(source.width, source.height, source.config)
        Utils.matToBitmap(tempMatrix1, resultBitmap)

        return resultBitmap
    }

    private fun distance(x1: Double, y1: Double, x2: Double, y2: Double): Double {
        return sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
    }

    private fun fastCos(x: Double): Double {
        var newX = x + 1.57079632
        if (newX > 3.14159265) newX -= 6.28318531
        return if (newX < 0) 1.27323954 * newX + 0.405284735 * newX * x else 1.27323954 * newX - 0.405284735 * newX * newX
    }
}
