package com.aqchen.filterfiesta.data.repository

import com.aqchen.filterfiesta.data.remote.FirebaseAuthentication
import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.aqchen.filterfiesta.util.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuthentication
): AuthRepository {
    override suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<FirebaseUser> {
        return try {
            Resource.Success(firebaseAuth.conn.signInWithEmailAndPassword(email, password).await().user!!)
        } catch (e: FirebaseException) {
            Resource.Error(e.message ?: "Unknown Firebase Auth Error")
        }
    }

    override suspend fun createAccountWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<FirebaseUser> {
        return try {
            Resource.Success(firebaseAuth.conn.createUserWithEmailAndPassword(email, password).await().user!!)
        } catch (e: FirebaseException) {
            Resource.Error(e.message ?: "Unknown Firebase Auth Error")
        }
    }

    override fun sendPasswordResetEmail(email: String): Task<Void> {
        return firebaseAuth.conn.sendPasswordResetEmail(email)
    }

    override fun signOut() {
        firebaseAuth.conn.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.conn.currentUser
    }

    override fun getAuthStateFlow(scope: CoroutineScope) = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            try {
                trySend(auth.currentUser)
            } catch (e: FirebaseException) {
                // Do nothing
            }
        }
        firebaseAuth.conn.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.conn.removeAuthStateListener(authStateListener)
        }
    }.stateIn(scope, SharingStarted.WhileSubscribed(), firebaseAuth.conn.currentUser)
}
