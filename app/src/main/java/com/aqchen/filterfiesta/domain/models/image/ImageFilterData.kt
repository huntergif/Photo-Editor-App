package com.aqchen.filterfiesta.domain.models.image

data class ImageFilterData(
    val type: String,
    val name: String,
    val parameters: Map<String, Double>
)