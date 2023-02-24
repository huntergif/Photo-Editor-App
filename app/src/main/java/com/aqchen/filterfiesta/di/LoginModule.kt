package com.aqchen.filterfiesta.di

import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.aqchen.filterfiesta.domain.use_case.auth.LoginWithEmailAndPasswordUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.ValidateEmailUseCase
import com.aqchen.filterfiesta.domain.use_case.auth.ValidatePasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import javax.inject.Singleton

@Module
@InstallIn(FragmentComponent::class)
object LoginModule {
    @Provides
    @Singleton
    fun provideValidateEmailUseCase(): ValidateEmailUseCase {
        return ValidateEmailUseCase()
    }

    @Provides
    @Singleton
    fun provideValidatePasswordUseCase(): ValidatePasswordUseCase {
        return ValidatePasswordUseCase()
    }

    @Provides
    @Singleton
    fun provideLoginWithEmailAndPasswordUseCase(authRepository: AuthRepository): LoginWithEmailAndPasswordUseCase {
        return LoginWithEmailAndPasswordUseCase(authRepository)
    }
}