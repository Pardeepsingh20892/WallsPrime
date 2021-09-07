package com.wallsprime.wallpapers

import android.app.Application

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class UnsplashApplication : Application() {
    override fun onCreate() {
        super.onCreate()
       // AppInitializer.getInstance(this).initializeComponent(CustomWorkManager::class.java)
    }


}