package com.aqchen.filterfiesta.data.repository

import com.aqchen.filterfiesta.data.dao.FilterGroupDao
import com.aqchen.filterfiesta.domain.models.FilterGroup
import com.aqchen.filterfiesta.domain.repository.FilterGroupRepository
import com.aqchen.filterfiesta.util.Resource
import com.google.firebase.FirebaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FilterGroupRepositoryImpl @Inject constructor(
    private val filterGroupDao: FilterGroupDao
): FilterGroupRepository {
    // Note that the Unit class is the standard "no return value" type class: https://stackoverflow.com/questions/55953052/kotlin-void-vs-unit-vs-nothing
    // Could also be Nothing, but doesn't seem to matter too much
    override suspend fun createUserFilterGroup(userId: String, filterGroup: FilterGroup): Resource<Unit> {
        return try {
            Resource.Success(
                filterGroupDao.createUserFilterGroup(userId, filterGroup)
            )
        } catch (e: FirebaseException) {
            Resource.Error("Failed to create filter group: " + (e.message ?: "Unkown Firestore error"))
        }
    }

    // Note the flow will always emit the list of all the FilterGroups for a user, even if only one FilterGroup changed
    override suspend fun getUserFilterGroups(userId: String): Resource<Flow<MutableList<FilterGroup>>> {
        return try {
            Resource.Success(
                filterGroupDao.getUserFilterGroupsFlow(userId)
                    .map { querySnapshot -> querySnapshot.toObjects(FilterGroup::class.java) }
            )
        } catch (e: FirebaseException) {
            Resource.Error("Failed to get filter groups: " + (e.message ?: "Unknown Firestore error"))
        }
    }

    override suspend fun updateUserFilterGroup(userId: String, filterGroup: FilterGroup): Resource<Unit> {
        return try {
            Resource.Success(filterGroupDao.updateUserFilterGroup(userId, filterGroup))
        } catch (e: FirebaseException) {
            Resource.Error("Failed to update filter group: " + (e.message ?: "Unknown Firestore error"))
        }
    }

    override suspend fun deleteUserFilterGroup(userId: String, filterGroupId: String): Resource<Unit> {
        return try {
            Resource.Success(filterGroupDao.deleteUserFilterGroup(userId, filterGroupId))
        } catch (e: FirebaseException) {
            Resource.Error("Failed to delete filter group: " + (e.message ?: "Unknown Firestore error"))
        }
    }
}
