package com.aqchen.filterfiesta.domain.models.image

data class ParameterSettings(
    val name: String,
    val min: Double,
    val max: Double,
    val default: Double,
)

abstract class BaseImageFilter(
    val type: String,
    val name: String,
    val parameterSettings: Map<String, ParameterSettings>
) {
    abstract fun apply(parameters: Map<String, Double>)
}
