package com.aqchen.filterfiesta.di

import com.aqchen.filterfiesta.data.remote.FirebaseFirestore
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
}
