package com.aqchen.filterfiesta.domain.use_case.auth

import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.aqchen.filterfiesta.util.Resource
import com.google.common.truth.Truth
import com.google.firebase.auth.FirebaseUser
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.Test

class GetAuthStateFlowUseCaseTest {

    private lateinit var getAuthStateFlowUseCase: GetAuthStateFlowUseCase
    private lateinit var authRepository: AuthRepository

    @Before
    fun setup() {
        authRepository = mockk()
        getAuthStateFlowUseCase = GetAuthStateFlowUseCase(authRepository)
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
    fun invoke_withEmailAndPassword_returnsStateFlow() {
        // setup
        val scope = mockk<CoroutineScope>()
        val result = mockk<StateFlow<FirebaseUser?>>()
        every { authRepository.getAuthStateFlow(scope) } returns result
        // run
        val response = runBlocking { getAuthStateFlowUseCase(scope) }
        // assert
        Truth.assertThat(response).isEqualTo(result)
        verify(exactly = 1) {
            authRepository.getAuthStateFlow(scope)
        }
    }
}
