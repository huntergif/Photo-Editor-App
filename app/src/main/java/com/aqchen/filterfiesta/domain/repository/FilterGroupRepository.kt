package com.aqchen.filterfiesta.domain.repository

import com.aqchen.filterfiesta.domain.models.FilterGroup
import com.aqchen.filterfiesta.util.Resource
import kotlinx.coroutines.flow.Flow

interface FilterGroupRepository {
    suspend fun createUserFilterGroup(userId: String, filterGroup: FilterGroup): Resource<Unit>
    suspend fun getUserFilterGroups(userId: String): Resource<Flow<MutableList<FilterGroup>>>
    suspend fun updateUserFilterGroup(userId: String, filterGroup: FilterGroup): Resource<Unit>
    suspend fun deleteUserFilterGroup(userId: String, filterGroupId: String): Resource<Unit>
}
