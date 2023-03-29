package com.aqchen.filterfiesta.domain.models.image

abstract class ImageFilterOrAdjustment private constructor() {
    abstract val type: String
    abstract val parameters: Map<String, Double>

    abstract fun build()
    abstract fun setParameters(parameters: Map<String, Double>)
    abstract fun apply()
}
