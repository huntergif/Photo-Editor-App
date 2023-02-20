package com.aqchen.filterfiesta.domain.use_case.auth

import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.aqchen.filterfiesta.util.Resource
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class CreateUserWithEmailAndPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Resource<FirebaseUser?> {
        return authRepository.createAccountWithEmailAndPassword(email, password)
    }
}
