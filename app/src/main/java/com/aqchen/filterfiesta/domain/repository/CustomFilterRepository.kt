package com.aqchen.filterfiesta.domain.repository

import com.aqchen.filterfiesta.domain.models.CustomFilter
import com.aqchen.filterfiesta.util.Resource
import kotlinx.coroutines.flow.Flow

interface CustomFilterRepository {
    suspend fun createUserCustomFilter(userId: String, customFilter: CustomFilter): Resource<Unit>
    // Needs to return MutableList or else use case can't infer type for some reason
    suspend fun getUserCustomFilters(userId: String): Resource<Flow<MutableList<CustomFilter>>>
    suspend fun getUserCustomFilter(userId: String, filterGroupId: String): Resource<Flow<CustomFilter?>>
    suspend fun updateUserCustomFilter(userId: String, customFilter: CustomFilter): Resource<Unit>
    suspend fun deleteUserCustomFilter(userId: String, filterGroupId: String): Resource<Unit>
}
