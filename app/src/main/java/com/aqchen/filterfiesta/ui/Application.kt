package com.aqchen.filterfiesta.ui

import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import org.opencv.android.OpenCVLoader

@HiltAndroidApp
class Application : android.app.Application() {
    @Override
    override fun onCreate() {
        super.onCreate()

        DynamicColors.applyToActivitiesIfAvailable(this)
        OpenCVLoader.initDebug()
    }
}
