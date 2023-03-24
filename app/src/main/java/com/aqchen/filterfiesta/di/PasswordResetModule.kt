package com.aqchen.filterfiesta.di

import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.aqchen.filterfiesta.domain.use_case.auth.CreateUserWithEmailAndPasswordUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.SendPasswordResetEmailUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.ValidateEmailUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.ValidatePasswordUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.ValidateRepeatedPasswordUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.ValidateTermsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import javax.inject.Singleton

@Module
@InstallIn(FragmentComponent::class)
object PasswordResetModule {

    @Provides
    @Singleton
    fun provideSendPasswordResetEmailUseCase(authRepository: AuthRepository): SendPasswordResetEmailUseCase {
        return SendPasswordResetEmailUseCase(authRepository)
    }
}