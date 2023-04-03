package com.aqchen.filterfiesta.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aqchen.filterfiesta.domain.use_case.auth.GetAuthStateFlowUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val getAuthStateFlowUseCase: GetAuthStateFlowUseCase,
) : ViewModel() {

    val authStateFlow = getAuthStateFlowUseCase(viewModelScope)

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SignOut -> {
                signOutUseCase()
            }
        }
    }
}
