package com.aqchen.filterfiesta.di

import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.aqchen.filterfiesta.domain.repository.FilterGroupRepository
import com.aqchen.filterfiesta.domain.use_case.auth.GetCurrentUserUseCase
import com.aqchen.filterfiesta.domain.use_case.filter_groups.CreateFilterGroupUseCase
import com.aqchen.filterfiesta.domain.use_case.filter_groups.DeleteFilterGroupUseCase
import com.aqchen.filterfiesta.domain.use_case.filter_groups.FilterGroupsUseCases
import com.aqchen.filterfiesta.domain.use_case.filter_groups.GetFilterGroupsUseCase
import com.aqchen.filterfiesta.domain.use_case.filter_groups.UpdateFilterGroupUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// These dependencies live for entire app lifetime

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGetCurrentUserUseCase(authRepository: AuthRepository): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(authRepository)
    }
}
