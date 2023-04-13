package com.aqchen.filterfiesta.domain.use_case.filters

import android.graphics.Bitmap
import com.aqchen.filterfiesta.util.bitmapConfigToCvType
import org.opencv.core.Mat

class CreateImageMatrixUseCase {
    operator fun invoke(bitmap: Bitmap): Mat {
        val cvType = bitmapConfigToCvType(bitmap.config)
        return Mat(bitmap.height, bitmap.width, cvType)
    }
}
