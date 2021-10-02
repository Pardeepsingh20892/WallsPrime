package com.wallsprime.wallpapers

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class UnsplashApplication : Application(){


    override fun onCreate() {
        super.onCreate()

        val sp = PreferenceManager.getDefaultSharedPreferences(this)

        val theme = when(sp.getString("theme","")){
            "default" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            "light_mode" -> AppCompatDelegate.MODE_NIGHT_NO
            "dark_mode" ->  AppCompatDelegate.MODE_NIGHT_YES
            else  -> AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(theme)

    }
}