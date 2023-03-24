package com.aqchen.filterfiesta.data.dao

import com.aqchen.filterfiesta.data.remote.FirebaseFirestore
import com.aqchen.filterfiesta.domain.models.CustomFilter
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilterGroupDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FilterGroupDao {
    private val userDataCollectionRef = firestore.conn.collection("userData")
    override suspend fun createUserFilterGroup(userId: String, customFilter: CustomFilter) {
        try {
            userDataCollectionRef.document(userId).collection("customFilters").add(
                customFilter
            )
        } catch (e: FirebaseException) {
            throw e
        }
    }

    override suspend fun getUserFilterGroupsFlow(userId: String): Flow<QuerySnapshot> {
        try {
            return userDataCollectionRef.document(userId).collection("customFilters").snapshots()
        } catch (e: FirebaseException) {
            throw e
        }
    }

    override suspend fun getUserFilterGroupFlow(
        userId: String,
        filterGroupId: String
    ): Flow<DocumentSnapshot> {
        try {
            return userDataCollectionRef.document(userId).collection("customFilters").document(filterGroupId).snapshots()
        } catch (e: FirebaseException) {
            throw e
        }
    }

    override suspend fun updateUserFilterGroup(userId: String, customFilter: CustomFilter) {
        if (customFilter.id == null) {
            // Note: FirebaseFirestoreException extends FirebaseException
            throw FirebaseFirestoreException(
                "FilterGroup id can't be null when updating.",
                FirebaseFirestoreException.Code.INVALID_ARGUMENT
            )
        }
        try {
            val doc = userDataCollectionRef.document(userId).collection("customFilters").document(customFilter.id)
            doc.set(customFilter)
        } catch (e: FirebaseException) {
            throw e
        }
    }

    override suspend fun deleteUserFilterGroup(userId: String, customFilterId: String) {
        try {
            val filterGroup = userDataCollectionRef.document(userId).collection("customFilters")
                .document(customFilterId)
            filterGroup.delete()
        } catch (e: FirebaseException) {
            throw e
        }
    }
}
