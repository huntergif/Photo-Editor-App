package com.aqchen.filterfiesta.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.aqchen.filterfiesta.util.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    // only this view model can mutate the state flow
    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser?>?>(null)
    // public login flow is not mutable
    val loginFlow: StateFlow<Resource<FirebaseUser?>?> = _loginFlow

    private val _signupFlow = MutableStateFlow<Resource<FirebaseUser?>?>(null)
    val signupFlow: StateFlow<Resource<FirebaseUser?>?> = _signupFlow

    val currentUser: FirebaseUser?
        get() = authRepository.getCurrentUser()

    init {
        if (authRepository.getCurrentUser() != null) {
            _loginFlow.value = Resource.Success(authRepository.getCurrentUser())
        }
    }

    // coroutine automatically canceled when view model is destroyed
    fun login(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = authRepository.signInWithEmailAndPassword(email, password)
        _loginFlow.value = result
    }

    fun signup(email: String, password: String) = viewModelScope.launch {
        _signupFlow.value = Resource.Loading
        val result = authRepository.createAccountWithEmailAndPassword(email, password)
        _signupFlow.value = result
    }

    fun signOut() {
        authRepository.signOut()
        _loginFlow.value = null
        _signupFlow.value = null
    }
}