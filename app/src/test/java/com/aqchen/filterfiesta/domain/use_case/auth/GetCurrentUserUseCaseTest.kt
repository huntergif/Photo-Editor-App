package com.aqchen.filterfiesta.domain.use_case.auth

import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.google.common.truth.Truth
import com.google.firebase.auth.FirebaseUser
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.Test

class GetCurrentUserUseCaseTest {

    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
    private lateinit var authRepository: AuthRepository

    @Before
    fun setup() {
        authRepository = mockk()
        getCurrentUserUseCase = GetCurrentUserUseCase(authRepository)
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
    fun invoke_withEmailAndPassword_returnsFirebaseUser() {
        // setup
        val result = mockk<FirebaseUser>()
        every { authRepository.getCurrentUser() } returns result
        // run
        val response = runBlocking { getCurrentUserUseCase() }
        // assert
        Truth.assertThat(response).isEqualTo(result)
        verify(exactly = 1) {
            authRepository.getCurrentUser()
        }
    }
}