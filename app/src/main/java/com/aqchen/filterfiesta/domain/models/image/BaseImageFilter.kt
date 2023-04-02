package com.aqchen.filterfiesta.domain.models.image

import android.graphics.Bitmap
import android.net.Uri
import com.aqchen.filterfiesta.domain.models.Filter
import com.aqchen.filterfiesta.util.Resource

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
    abstract fun apply(source: Bitmap, parameters: Map<String, Double>): Bitmap?
}

fun newDefaultFilter(filter: BaseImageFilter): Filter {
    val paramMap = mutableMapOf<String, Double>()
    filter.parameterSettings.forEach {
        paramMap[it.type] = it.default
    }
    return Filter(filter.type, paramMap.toMap())
}
