package com.aqchen.filterfiesta.domain.repository

import com.aqchen.filterfiesta.util.Resource
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<FirebaseUser?>
    suspend fun createAccountWithEmailAndPassword(email: String, password: String): Resource<FirebaseUser?>
    fun signOut()
    fun getCurrentUser(): FirebaseUser?
}
