package com.aqchen.filterfiesta.di

import com.aqchen.filterfiesta.data.dao.FilterGroupDao
import com.aqchen.filterfiesta.data.dao.FilterGroupDaoImpl
import com.aqchen.filterfiesta.data.remote.FirebaseFirestore
import com.aqchen.filterfiesta.data.repository.FilterGroupRepositoryImpl
import com.aqchen.filterfiesta.domain.repository.FilterGroupRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.FragmentComponent
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
}
