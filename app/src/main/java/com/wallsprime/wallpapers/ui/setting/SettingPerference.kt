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

class SettingPerference : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {

    private lateinit var navController: NavController
    private lateinit var listener : NavController.OnDestinationChangedListener



    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)


       val autoWallpaperChanger =  findPreference<Preference>("Auto_change_wallpaper")

        autoWallpaperChanger?.onPreferenceClickListener= this


    }

    override fun onPreferenceClick(preference: Preference?): Boolean {


        val direction = SettingPerferenceDirections.actionSettingPerferenceToWallpaperFragment()
        findNavController().navigate(direction)


       return true
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbarWallpaperSetting)


        navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)


        listener = NavController.OnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.settingPerference) {

                toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24_explore)

            }

        }
        navController.addOnDestinationChangedListener(listener)



    }



    override fun onDestroyView() {
        super.onDestroyView()

        navController.removeOnDestinationChangedListener(listener)


    }

}