package com.aqchen.filterfiesta.domain.use_case.auth

import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetAuthStateFlowUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(scope: CoroutineScope): StateFlow<FirebaseUser?> {
        return authRepository.getAuthStateFlow(scope)
    }
}
