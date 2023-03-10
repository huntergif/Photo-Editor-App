package com.aqchen.filterfiesta.data.dao

import com.aqchen.filterfiesta.domain.models.CustomFilter
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface FilterGroupDao {
    suspend fun createUserFilterGroup(userId: String, customFilter: CustomFilter)
    suspend fun getUserFilterGroupsFlow(userId: String): Flow<QuerySnapshot>

    suspend fun getUserFilterGroupFlow(userId: String, filterGroupId: String): Flow<DocumentSnapshot>
    suspend fun updateUserFilterGroup(userId: String, customFilter: CustomFilter)
    suspend fun deleteUserFilterGroup(userId: String, filterGroupId: String)
}
