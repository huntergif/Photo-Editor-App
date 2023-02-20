package com.aqchen.filterfiesta.ui

import dagger.hilt.android.HiltAndroidApp
import org.opencv.android.OpenCVLoader

@HiltAndroidApp
class Application : android.app.Application() {
    @Override
    override fun onCreate() {
        super.onCreate()

        OpenCVLoader.initDebug()
    }
}
