package com.aqchen.filterfiesta.ui.login

import androidx.lifecycle.ViewModel
import com.aqchen.filterfiesta.domain.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// Annotations to signal to Hilt to inject dependencies in constructor
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository
): ViewModel() {
}
