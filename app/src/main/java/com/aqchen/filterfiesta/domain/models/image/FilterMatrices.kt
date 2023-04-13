package com.aqchen.filterfiesta.domain.models.image

import org.opencv.core.Mat

data class FilterMatrices(
    val resultMatrix: Mat,
    val matrix1: Mat,
    val matrix2: Mat,
    val matrix3: Mat,
)
