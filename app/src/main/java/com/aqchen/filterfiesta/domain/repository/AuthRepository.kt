package com.aqchen.filterfiesta.domain.repository

import com.aqchen.filterfiesta.util.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<FirebaseUser?>
    suspend fun createAccountWithEmailAndPassword(email: String, password: String): Resource<FirebaseUser?>
    fun sendPasswordResetEmail(email: String): Task<Void>
    fun signOut()
    fun getCurrentUser(): FirebaseUser?
}
