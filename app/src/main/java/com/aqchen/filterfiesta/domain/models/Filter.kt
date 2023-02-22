package com.aqchen.filterfiesta.domain.models

import com.google.firebase.firestore.IgnoreExtraProperties

// Filter POJO
@IgnoreExtraProperties
data class Filter(
    val id: String? = null,
    var strength: Int = 0,
) {
    companion object {
        const val FIELD_ID = "id"
        const val FIELD_STRENGTH = "strength"
    }
}
