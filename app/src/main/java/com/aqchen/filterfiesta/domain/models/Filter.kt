package com.aqchen.filterfiesta.domain.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

// Filter POJO
@IgnoreExtraProperties
data class Filter(
    val type: String = "",
    val parameters: Map<String, Double> = mutableMapOf(),
) {
    companion object {
        const val FIELD_TYPE = "type"
        const val FIELD_PARAMETERS = "parameters"
    }
}
