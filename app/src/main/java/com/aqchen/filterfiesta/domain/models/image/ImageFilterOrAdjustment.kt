package com.aqchen.filterfiesta.domain.models.image

abstract class ImageFilter private constructor() {
    val type: String
    val parameters: Map<String, Double>

    fun applyFilter()
}
