package com.aqchen.filterfiesta.di

import com.aqchen.filterfiesta.data.dao.FilterGroupDao
import com.aqchen.filterfiesta.data.dao.FilterGroupDaoImpl
import com.aqchen.filterfiesta.data.remote.FirebaseFirestore
import com.aqchen.filterfiesta.data.repository.FilterGroupRepositoryImpl
import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.aqchen.filterfiesta.domain.repository.FilterGroupRepository
import com.aqchen.filterfiesta.domain.use_case.filter_groups.CreateFilterGroupUseCase
import com.aqchen.filterfiesta.domain.use_case.filter_groups.DeleteFilterGroupUseCase
import com.aqchen.filterfiesta.domain.use_case.filter_groups.FilterGroupsUseCases
import com.aqchen.filterfiesta.domain.use_case.filter_groups.GetFilterGroupsUseCase
import com.aqchen.filterfiesta.domain.use_case.filter_groups.UpdateFilterGroupUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Singleton

@Module
@InstallIn(FragmentComponent::class)
object PhotoEditorModule {
    @Provides
    // Determines scope - singleton means we only inject one and the same instance of the dependency
    @Singleton
    fun provideFireStore(): FirebaseFirestore {
        return FirebaseFirestore()
    }

    @Provides
    @Singleton
    fun provideFilterGroupDao(firestore: FirebaseFirestore): FilterGroupDao {
        return FilterGroupDaoImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideFilterGroupRepository(filterGroupDao: FilterGroupDao): FilterGroupRepository {
        return FilterGroupRepositoryImpl(filterGroupDao)
    }

    @Provides
    @Singleton
    fun provideFilterGroupsUseCases(filterGroupRepository: FilterGroupRepository, authRepository: AuthRepository): FilterGroupsUseCases {
        return FilterGroupsUseCases(
            createFilterGroupUseCase = CreateFilterGroupUseCase(filterGroupRepository, authRepository),
            deleteFilterGroupUseCase = DeleteFilterGroupUseCase(filterGroupRepository, authRepository),
            getFilterGroupsUseCase = GetFilterGroupsUseCase(filterGroupRepository, authRepository),
            updateFilterGroupUseCase = UpdateFilterGroupUseCase(filterGroupRepository, authRepository),
        )
    }
}
