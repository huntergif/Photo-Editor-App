package com.aqchen.filterfiesta.di

import com.aqchen.filterfiesta.data.dao.FilterGroupDao
import com.aqchen.filterfiesta.data.dao.FilterGroupDaoImpl
import com.aqchen.filterfiesta.data.remote.FirebaseFirestore
import com.aqchen.filterfiesta.data.repository.CustomCustomFilterRepositoryImpl
import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.aqchen.filterfiesta.domain.repository.CustomFilterRepository
import com.aqchen.filterfiesta.domain.use_case.custom_filters.CreateCustomFilterUseCase
import com.aqchen.filterfiesta.domain.use_case.custom_filters.DeleteCustomFilterUseCase
import com.aqchen.filterfiesta.domain.use_case.custom_filters.CustomFiltersUseCases
import com.aqchen.filterfiesta.domain.use_case.custom_filters.GetCustomFilterUseCase
import com.aqchen.filterfiesta.domain.use_case.custom_filters.GetCustomFiltersUseCase
import com.aqchen.filterfiesta.domain.use_case.custom_filters.UpdateCustomFilterUseCase
import com.aqchen.filterfiesta.domain.use_case.custom_filters.ValidateCustomFilterNameUseCase
import com.aqchen.filterfiesta.domain.use_case.custom_filters.ValidateCustomFilterUpdatableUseCase
import com.aqchen.filterfiesta.domain.use_case.tool_pages.GetToolPagesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object PhotoEditorModule {
    @Provides
    // Determines scope - singleton means we only inject one and the same instance of the dependency
    @ViewModelScoped
    fun provideFireStore(): FirebaseFirestore {
        return FirebaseFirestore()
    }

    @Provides
    @ViewModelScoped
    fun provideFilterGroupDao(firestore: FirebaseFirestore): FilterGroupDao {
        return FilterGroupDaoImpl(firestore)
    }

    @Provides
    @ViewModelScoped
    fun provideFilterGroupRepository(filterGroupDao: FilterGroupDao): CustomFilterRepository {
        return CustomCustomFilterRepositoryImpl(filterGroupDao)
    }

    @Provides
    @ViewModelScoped
    fun provideFilterGroupsUseCases(customFilterRepository: CustomFilterRepository, authRepository: AuthRepository): CustomFiltersUseCases {
        return CustomFiltersUseCases(
            createFilterGroupUseCase = CreateCustomFilterUseCase(customFilterRepository, authRepository),
            deleteCustomFilterUseCase = DeleteCustomFilterUseCase(customFilterRepository, authRepository),
            getCustomFiltersUseCase = GetCustomFiltersUseCase(customFilterRepository, authRepository),
            updateFilterGroupUseCase = UpdateCustomFilterUseCase(customFilterRepository, authRepository),
        )
    }

    @Provides
    @ViewModelScoped
    fun provideGetToolPagesUsesCase(): GetToolPagesUseCase {
        return GetToolPagesUseCase()
    }

    @Provides
    @ViewModelScoped
    fun provideValidateCustomFilterNameUseCase(): ValidateCustomFilterNameUseCase {
        return ValidateCustomFilterNameUseCase()
    }

    @Provides
    @ViewModelScoped
    fun providesGetCustomFilterUseCase(customFilterRepository: CustomFilterRepository, authRepository: AuthRepository): GetCustomFilterUseCase {
        return GetCustomFilterUseCase(customFilterRepository, authRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesValidateCustomFilterUpdatableUseCase(): ValidateCustomFilterUpdatableUseCase {
        return ValidateCustomFilterUpdatableUseCase()
    }
}
