package com.aqchen.filterfiesta.domain.models.image

data class ParameterSetting(
    val type: String,
    val name: String,
    val min: Double,
    val max: Double,
    val default: Double,
)

abstract class BaseImageFilter(
    val type: String,
    val name: String,
    val parameterSettings: List<ParameterSetting>
) {
    abstract fun apply(parameters: Map<String, Double>)
}
