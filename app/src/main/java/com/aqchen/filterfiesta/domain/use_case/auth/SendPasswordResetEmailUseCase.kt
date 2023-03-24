package com.aqchen.filterfiesta.domain.use_case.auth

import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.aqchen.filterfiesta.util.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class SendPasswordResetEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String): Task<Void> {
        return authRepository.sendPasswordResetEmail(email)
    }
}