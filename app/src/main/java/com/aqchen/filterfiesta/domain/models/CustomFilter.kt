package com.aqchen.filterfiesta.domain.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

// FilterGroup POJO
@IgnoreExtraProperties
data class CustomFilter(
    @ServerTimestamp var timestamp: Date? = null,
    @DocumentId val id: String? = null,
    val userId: String = "",
    val name: String = "",
    val isPublic: Boolean = false,
    val filters: List<Filter> = emptyList(),
) {
    companion object {
        const val FIELD_USER_ID = "userId"
        const val FIELD_NAME = "name"
        const val FIELD_IS_PUBLIC = "isPublic"
        const val FIELD_FILTERS = "filters"
    }
}
