package com.aqchen.filterfiesta.domain.models

import com.google.firebase.firestore.IgnoreExtraProperties

// FilterGroup POJO
@IgnoreExtraProperties
data class FilterGroup(
    val id: String? = null,
    val userId: String = "",
    val name: String = "",
    val isPublic: Boolean = false,
    val filters: MutableList<Filter> = mutableListOf(),
) {
    companion object {
        const val FIELD_ID = "id"
        const val FIELD_STRENGTH = "strength"
    }
}
