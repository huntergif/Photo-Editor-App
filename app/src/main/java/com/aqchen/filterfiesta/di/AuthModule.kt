package com.aqchen.filterfiesta.di

import com.aqchen.filterfiesta.data.remote.FirebaseAuth
import com.aqchen.filterfiesta.data.repository.FirebaseAuthRepositoryImpl
import com.aqchen.filterfiesta.domain.repository.FirebaseAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
// Component determines lifetime of dependencies - singleton lasts as long as application
@InstallIn(SingletonComponent::class)
object AuthModule {
    // This function provides a dependency
    @Provides
    // Determines scope - singleton means we only inject one and the same instance of the dependency
    @Singleton
    fun provideAuth(): FirebaseAuth {
        return FirebaseAuth()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthRepository(firebaseAuth: FirebaseAuth): FirebaseAuthRepository {
        return FirebaseAuthRepositoryImpl(firebaseAuth)
    }
}
