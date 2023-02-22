package com.aqchen.filterfiesta.data.dao

import com.aqchen.filterfiesta.domain.models.FilterGroup
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface FilterGroupDao {
    suspend fun createFilterGroupDocForUserId(userId: String, filterGroup: FilterGroup)
    suspend fun getFilterGroupDocsByUserIdFlow(userId: String): Flow<QuerySnapshot>
    suspend fun updateFilterGroupDocForUserId(userId: String, filterGroup: FilterGroup)
    suspend fun deleteFilterGroupDocForUserId(userId: String, filterGroupDocId: String)
}
