package com.aqchen.filterfiesta.di

import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.aqchen.filterfiesta.domain.use_case.auth.CreateUserWithEmailAndPasswordUseCase
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
class RegisterModule {
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
    fun provideValidateRepeatedPasswordUseCase(): ValidateRepeatedPasswordUseCase {
        return ValidateRepeatedPasswordUseCase()
    }

    @Provides
    @Singleton
    fun provideValidateTermsUseCase(): ValidateTermsUseCase {
        return ValidateTermsUseCase()
    }

    @Provides
    @Singleton
    fun provideCreateUserWithEmailAndPasswordUseCase(authRepository: AuthRepository): CreateUserWithEmailAndPasswordUseCase {
        return CreateUserWithEmailAndPasswordUseCase(authRepository)
    }
}