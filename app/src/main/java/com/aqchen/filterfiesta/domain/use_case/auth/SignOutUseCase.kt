package com.aqchen.filterfiesta.domain.use_case.auth

import com.aqchen.filterfiesta.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() {
        authRepository.signOut()
    }
}