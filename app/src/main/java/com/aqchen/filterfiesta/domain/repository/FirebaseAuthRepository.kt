package com.aqchen.filterfiesta.domain.repository

interface FirebaseAuthRepository {
    suspend fun signInWithEmailAndPassword()
    suspend fun signOut()
}
