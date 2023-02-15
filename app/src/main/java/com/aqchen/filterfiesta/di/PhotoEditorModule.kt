package com.aqchen.filterfiesta.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
// ActivityRetained lives for lifetime of Activity and across configuration changes
@InstallIn(ActivityRetainedComponent::class)
object PhotoEditorModule {

}
