package com.aqchen.filterfiesta.domain.models

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.type.Date

// Filter POJO
@IgnoreExtraProperties
data class FilterGroup(
    val id: String? = null,
    var userId: String = "",
    var name: String = "",
    var isPublic: Boolean = false,
    var filters: ArrayList<Filter> = ArrayList(),
) {
    companion object {
        const val FIELD_ID = "id"
        const val FIELD_STRENGTH = "strength"
    }
}
