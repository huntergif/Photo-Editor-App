package com.aqchen.filterfiesta.data.repository

import com.aqchen.filterfiesta.data.remote.FirebaseAuthentication
import com.aqchen.filterfiesta.util.Resource
import com.google.common.truth.Truth
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplTest {

    private lateinit var authRepository: AuthRepositoryImpl
    private lateinit var firebaseAuth: FirebaseAuthentication
    private lateinit var firebaseConn: FirebaseAuth

    @Before
    fun setup() {
        firebaseAuth = mockk()
        firebaseConn = mockk()
        every {
            firebaseAuth.conn
        } returns firebaseConn
        authRepository = AuthRepositoryImpl(firebaseAuth)
    }

    @After
    fun clearAll() {
        clearAllMocks()
    }

    companion object {
        @JvmStatic
        @AfterClass
        fun cleanup() {
            unmockkAll()
        }
    }

    @Test
    fun signInWithEmailAndPassword_whenConnReturnsUser_returnSuccess() {
        // setup
        val email = "testEmail"
        val password = "testPassword"
        val mockUser = mockk<FirebaseUser>()
        val mockAuthResult = mockk<AuthResult>()
        every {
            mockAuthResult.user
        } returns mockUser
        // needed to mock await()
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        coEvery { firebaseConn.signInWithEmailAndPassword(email, password).await() }.coAnswers {
            mockAuthResult
        }

        // run
        val response = runBlocking { authRepository.signInWithEmailAndPassword(email, password) }

        // assert
        Truth.assertThat(response is Resource.Success).isTrue()
        Truth.assertThat((response as Resource.Success).data).isEqualTo(mockUser)
        coVerify(exactly = 1) {
            firebaseConn.signInWithEmailAndPassword(email, password)
        }
    }

    @Test
    fun signInWithEmailAndPassword_whenConnThrowsException_returnError() {
        // setup
        val email = "testEmail"
        val password = "testPassword"
        val mockUser = mockk<FirebaseUser>()
        val mockAuthResult = mockk<AuthResult>()
        every {
            mockAuthResult.user
        } returns mockUser
        // needed to mock await()
        every { firebaseConn.signInWithEmailAndPassword(email, password) } throws FirebaseAuthException("", "")

        // run
        val response = runBlocking { authRepository.signInWithEmailAndPassword(email, password) }

        // assert
        Truth.assertThat(response is Resource.Error).isTrue()
        coVerify(exactly = 1) {
            firebaseConn.signInWithEmailAndPassword(email, password)
        }
    }
}