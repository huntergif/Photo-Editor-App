package com.aqchen.filterfiesta.domain.models.image.preset_filters

import android.graphics.Bitmap
import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter
import com.aqchen.filterfiesta.domain.models.image.ParameterSetting
import com.aqchen.filterfiesta.util.bitmapConfigToCvType
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import org.opencv.photo.Photo

class ColorPencilPresetFilter: BaseImageFilter(
    type = "colorPencil",
    name = "Color Pencil",
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
            default = 0.07,
            min = 0.0,
            max = 1.0,
        ),
        ParameterSetting(
            type = "shadeFactor",
            name = "Shade Factor",
            default = 0.02,
            min = 0.0,
            max = 0.1,
        ),
    )
) {
    override fun apply(source: Bitmap, parameters: Map<String, Double>): Bitmap? {
        val cvType = bitmapConfigToCvType(source.config)
        val imageMatrix = Mat(source.height, source.width, cvType)
        val tempMatrix1 = Mat(source.height, source.width, cvType)
        val tempMatrix2 = Mat(source.height, source.width, cvType) // holds result

        val sigmaSpace = parameters["sigmaSpace"]?.toFloat() ?: return null
        val sigmaColor = parameters["sigmaColor"]?.toFloat() ?: return null
        val shadeFactor = parameters["shadeFactor"]?.toFloat() ?: return null

        Utils.bitmapToMat(source, imageMatrix)

        val origSize = imageMatrix.size()

        Imgproc.pyrDown(imageMatrix, imageMatrix)
        // MUST CONVERT INPUT TO RGB TO FIX DIMENSION/OVERLAP ISSUES
        Imgproc.cvtColor(imageMatrix, imageMatrix, Imgproc.COLOR_BGRA2RGB)
        Photo.pencilSketch(imageMatrix, tempMatrix1, tempMatrix2, sigmaSpace, sigmaColor, shadeFactor)
        Imgproc.cvtColor(tempMatrix2, tempMatrix2, Imgproc.COLOR_RGB2BGRA);
        Imgproc.pyrUp(tempMatrix2, tempMatrix2)
        Imgproc.resize(tempMatrix2, tempMatrix2, origSize)

        val resultBitmap = Bitmap.createBitmap(source.width, source.height, source.config)
        Utils.matToBitmap(tempMatrix2, resultBitmap)

        return resultBitmap
    }
}
