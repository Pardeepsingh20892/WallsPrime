package com.wallsprime.wallpapers.ui.setting

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.appbar.MaterialToolbar
import com.wallsprime.wallpapers.R

class SettingPreference : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {

    private lateinit var navController: NavController




    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preferences, rootKey)

       val autoWallpaperChanger =  findPreference<Preference>("Auto_change_wallpaper")
        autoWallpaperChanger?.onPreferenceClickListener= this


    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        val direction = SettingPreferenceDirections.actionSettingPerferenceToWallpaperFragment()
        findNavController().navigate(direction)
        return true
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbarWallpaperSetting)

        navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)


    }


}