package com.aqchen.filterfiesta.domain.models

import com.google.firebase.firestore.IgnoreExtraProperties

// Filter POJO
@IgnoreExtraProperties
data class Filter(
    val id: String? = null,
    val type: String = "",
    val parameters: Map<String, Float> = mutableMapOf(),
) {
    companion object {
        const val FIELD_ID = "id"
        const val FIELD_TYPE = "type"
        const val FIELD_PARAMETERS = "parameters"
    }
}
