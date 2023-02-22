package com.aqchen.filterfiesta.data.dao

import com.aqchen.filterfiesta.data.remote.FirebaseFirestore
import com.aqchen.filterfiesta.domain.models.FilterGroup
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.flow.Flow

class FilterGroupDaoImpl(
    private val fireStore: FirebaseFirestore
) : FilterGroupDao {
    override suspend fun createFilterGroupDocForUserId(userId: String, filterGroup: FilterGroup) {
        fireStore.conn.collection("userData").document(userId).collection("filterGroups").add(
            filterGroup
        )
    }

    override suspend fun getFilterGroupDocsByUserIdFlow(userId: String): Flow<QuerySnapshot> {
        return fireStore.conn.collection("userData").document(userId).collection("filterGroups").snapshots()
    }

    override suspend fun updateFilterGroupDocForUserId(userId: String, filterGroup: FilterGroup) {
        if (filterGroup.id == null) {
            throw FirebaseFirestoreException("filterGroup id can't be null", FirebaseFirestoreException.Code.INVALID_ARGUMENT)
        }
        //fireStore.conn.collection("userData").document(userId).collection("filterGroup").document(filterGroup.id).update(filterGroup)
    }

    override suspend fun deleteFilterGroupDocForUserId(userId: String, filterGroupDocId: String) {
        TODO("Not yet implemented")
    }
}
