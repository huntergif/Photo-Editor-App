package com.aqchen.filterfiesta.data.dao

import com.aqchen.filterfiesta.domain.models.FilterGroup
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface FilterGroupDao {
    suspend fun createUserFilterGroup(userId: String, filterGroup: FilterGroup)
    suspend fun getUserFilterGroupsFlow(userId: String): Flow<QuerySnapshot>
    suspend fun updateUserFilterGroup(userId: String, filterGroup: FilterGroup)
    suspend fun deleteUserFilterGroup(userId: String, filterGroupId: String)
}
