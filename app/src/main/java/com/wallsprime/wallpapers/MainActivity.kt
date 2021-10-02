package com.wallsprime.wallpapers







import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsControllerCompat.*
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.wallsprime.wallpapers.databinding.ActivityMainBinding
import com.wallsprime.wallpapers.utils.NetworkStateChecker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import android.content.Intent





@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {


    private lateinit var binding: ActivityMainBinding
    private lateinit var sp: SharedPreferences





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sp = PreferenceManager.getDefaultSharedPreferences(this)
        sp.registerOnSharedPreferenceChangeListener(this)



/*


        val networkStateChecker= NetworkStateChecker(applicationContext)


        networkStateChecker.observe(this, { isConnected ->

            if (isConnected){
                Log.i("ABC", "connected ")

            }
            else{

                Log.i("ABC", "not connected ")

            }


        })



*/




    }



    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

        val intent = intent
        finish()
        startActivity(intent)


        if(key == "theme"){
        val theme = when(sp.getString("theme","")){
            "default" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            "light_mode" -> AppCompatDelegate.MODE_NIGHT_NO
            "dark_mode" ->  AppCompatDelegate.MODE_NIGHT_YES
            else  -> AppCompatDelegate.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(theme)

        }


    }




    override fun onResume() {
        super.onResume()
       // hideSystemUI()
    }


    override fun onDestroy() {
        super.onDestroy()
       // showSystemUI()
        sp.unregisterOnSharedPreferenceChangeListener(this)

    }




    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.statusBars())
            controller.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, binding.root).show(WindowInsetsCompat.Type.statusBars())
    }







}












