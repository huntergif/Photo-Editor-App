package com.aqchen.filterfiesta.data.repository

import com.aqchen.filterfiesta.data.remote.FirebaseAuth
import com.aqchen.filterfiesta.domain.repository.FirebaseAuthRepository

class FirebaseAuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
): FirebaseAuthRepository {
    override suspend fun signInWithEmailAndPassword() {
        TODO("Not yet implemented")
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }
}
