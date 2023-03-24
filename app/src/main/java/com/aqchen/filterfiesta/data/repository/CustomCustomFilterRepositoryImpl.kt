package com.aqchen.filterfiesta.data.repository

import com.aqchen.filterfiesta.data.dao.FilterGroupDao
import com.aqchen.filterfiesta.domain.models.CustomFilter
import com.aqchen.filterfiesta.domain.repository.CustomFilterRepository
import com.aqchen.filterfiesta.util.Resource
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onErrorReturn
import javax.inject.Inject

class CustomCustomFilterRepositoryImpl @Inject constructor(
    private val filterGroupDao: FilterGroupDao
): CustomFilterRepository {
    // Note that the Unit class is the standard "no return value" type class: https://stackoverflow.com/questions/55953052/kotlin-void-vs-unit-vs-nothing
    // Could also be Nothing, but doesn't seem to matter too much
    override suspend fun createUserCustomFilter(userId: String, customFilter: CustomFilter): Resource<Unit> {
        return try {
            Resource.Success(
                filterGroupDao.createUserFilterGroup(userId, customFilter)
            )
        } catch (e: FirebaseException) {
            Resource.Error("Failed to create filter group: " + (e.message ?: "Unknown Firestore error"))
        }
    }

    // Note the flow will always emit the list of all the FilterGroups for a user, even if only one FilterGroup changed
    override suspend fun getUserCustomFilters(userId: String): Resource<Flow<MutableList<CustomFilter>>> {
        return try {
            Resource.Success(
                filterGroupDao.getUserFilterGroupsFlow(userId)
                    .map { querySnapshot -> querySnapshot.toObjects(CustomFilter::class.java) }
            )
        } catch (e: FirebaseException) {
            Resource.Error("Failed to get filter groups: " + (e.message ?: "Unknown Firestore error"))
        }
    }

    // nullable CustomFilter since the document may not exist
    override suspend fun getUserCustomFilter(
        userId: String,
        filterGroupId: String
    ): Resource<Flow<CustomFilter?>> {
        return try {
            Resource.Success(
                filterGroupDao.getUserFilterGroupFlow(userId, filterGroupId)
                    .map { documentSnapshot -> documentSnapshot.toObject(CustomFilter::class.java) }
            )
        } catch (e: FirebaseException) {
            Resource.Error("Failed to get filter groups: " + (e.message ?: "Unknown Firestore error"))
        }
    }

    override suspend fun updateUserCustomFilter(userId: String, customFilter: CustomFilter): Resource<Unit> {
        return try {
            Resource.Success(filterGroupDao.updateUserFilterGroup(userId, customFilter))
        } catch (e: FirebaseException) {
            Resource.Error("Failed to update filter group: " + (e.message ?: "Unknown Firestore error"))
        }
    }

    override suspend fun deleteUserCustomFilter(userId: String, filterGroupId: String): Resource<Unit> {
        return try {
            Resource.Success(filterGroupDao.deleteUserFilterGroup(userId, filterGroupId))
        } catch (e: FirebaseException) {
            Resource.Error("Failed to delete filter group: " + (e.message ?: "Unknown Firestore error"))
        }
    }
}
