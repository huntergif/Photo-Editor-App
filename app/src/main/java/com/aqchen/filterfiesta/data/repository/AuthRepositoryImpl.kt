package com.aqchen.filterfiesta.data.repository

import com.aqchen.filterfiesta.data.remote.FirebaseAuthentication
import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.aqchen.filterfiesta.util.Resource
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuthentication
): AuthRepository {
    override suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<FirebaseUser?> {
        return try {
            Resource.Success(firebaseAuth.conn.signInWithEmailAndPassword(email, password).await().user)
        } catch (e: FirebaseAuthException) {
            Resource.Error(e)
        }
    }

    override suspend fun createAccountWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<FirebaseUser?> {
        return try {
            Resource.Success(firebaseAuth.conn.createUserWithEmailAndPassword(email, password).await().user)
        } catch (e: FirebaseAuthException) {
            Resource.Error(e)
        }
    }

    override fun signOut() {
        firebaseAuth.conn.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.conn.currentUser
    }
}
