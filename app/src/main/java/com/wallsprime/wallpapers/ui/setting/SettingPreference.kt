package com.wallsprime.wallpapers.ui.setting

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.android.material.appbar.MaterialToolbar
import com.wallsprime.wallpapers.R

class SettingPreference : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private lateinit var navController: NavController
    private lateinit var sp: SharedPreferences



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


        sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sp.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {


        if(p1 == "theme"){

            val theme = when(sp.getString("theme","")){
                "default" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                "light_mode" -> AppCompatDelegate.MODE_NIGHT_NO
                "dark_mode" ->  AppCompatDelegate.MODE_NIGHT_YES
                else  -> AppCompatDelegate.MODE_NIGHT_NO

            }

            AppCompatDelegate.setDefaultNightMode(theme)

           // val activity = requireActivity()
           // val intent = activity.intent
           // activity.finish()
           // activity.startActivity(intent)


        }



    }

    override fun onDestroy() {
        super.onDestroy()
        sp.unregisterOnSharedPreferenceChangeListener(this)
    }

}