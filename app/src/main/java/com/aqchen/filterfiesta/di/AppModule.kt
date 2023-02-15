package com.aqchen.filterfiesta.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// These dependencies live for entire app lifetime

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

}
