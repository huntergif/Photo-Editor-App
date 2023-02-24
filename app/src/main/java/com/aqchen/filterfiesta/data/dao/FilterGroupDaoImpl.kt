package com.aqchen.filterfiesta.data.dao

import com.aqchen.filterfiesta.data.remote.FirebaseFirestore
import com.aqchen.filterfiesta.domain.models.FilterGroup
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilterGroupDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FilterGroupDao {
    private val userDataCollectionRef = firestore.conn.collection("userData")
    override suspend fun createUserFilterGroup(userId: String, filterGroup: FilterGroup) {
        try {
            userDataCollectionRef.document(userId).collection("filterGroups").add(
                filterGroup
            )
        } catch (e: FirebaseException) {
            throw e
        }
    }

    override suspend fun getUserFilterGroupsFlow(userId: String): Flow<QuerySnapshot> {
        try {
            return userDataCollectionRef.document(userId).collection("filterGroups").snapshots()
        } catch (e: FirebaseException) {
            throw e
        }
    }

    override suspend fun updateUserFilterGroup(userId: String, filterGroup: FilterGroup) {
        if (filterGroup.id == null) {
            // Note: FirebaseFirestoreException extends FirebaseException
            throw FirebaseFirestoreException(
                "FilterGroup id can't be null when updating.",
                FirebaseFirestoreException.Code.INVALID_ARGUMENT
            )
        }
        try {
            val doc = userDataCollectionRef.document(userId).collection("filterGroups").document(filterGroup.id)
            doc.set(filterGroup)
        } catch (e: FirebaseException) {
            throw e
        }
    }

    override suspend fun deleteUserFilterGroup(userId: String, filterGroupId: String) {
        try {
            val filterGroup = userDataCollectionRef.document(userId).collection("filterGroups")
                .document(filterGroupId)
            filterGroup.delete()
        } catch (e: FirebaseException) {
            throw e
        }
    }
}
