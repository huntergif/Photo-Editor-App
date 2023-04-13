package com.aqchen.filterfiesta.domain.use_case.auth

import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.aqchen.filterfiesta.util.Resource
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.Test

class CreateUserWithEmailAndPasswordUseCaseTest {

    private lateinit var createUserWithEmailAndPasswordUseCase: CreateUserWithEmailAndPasswordUseCase
    private lateinit var authRepository: AuthRepository

    @Before
    fun setup() {
        authRepository = mockk()
        createUserWithEmailAndPasswordUseCase = CreateUserWithEmailAndPasswordUseCase(authRepository)
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
    fun invoke_withEmailAndPassword_returnsResource() {
        // setup
        val email = "testEmail"
        val password = "testPassword"
        val result = mockk<Resource<FirebaseUser?>>()
        coEvery { authRepository.createAccountWithEmailAndPassword(email, password) } returns result
        // run
        val response = runBlocking { createUserWithEmailAndPasswordUseCase(email, password) }
        // assert
        assertThat(response).isEqualTo(result)
        coVerify(exactly = 1) {
            authRepository.createAccountWithEmailAndPassword(email, password)
        }
    }
}
