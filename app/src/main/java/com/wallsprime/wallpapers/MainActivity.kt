package com.wallsprime.wallpapers







import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.wallsprime.wallpapers.databinding.ActivityMainBinding
import com.wallsprime.wallpapers.utils.NetworkStateChecker
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {


    private lateinit var binding: ActivityMainBinding
    private lateinit var sp: SharedPreferences
    private lateinit var cm: ConnectivityManager
    private lateinit var networkStateChecker: NetworkStateChecker




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sp = PreferenceManager.getDefaultSharedPreferences(this)

        val theme = when(sp.getString("theme","")){
            "default" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            "light_mode" -> AppCompatDelegate.MODE_NIGHT_NO
            "dark_mode" ->  AppCompatDelegate.MODE_NIGHT_YES
            else  -> AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(theme)

        sp.registerOnSharedPreferenceChangeListener(this)





        networkStateChecker = NetworkStateChecker()
        cm = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkChangeFilter: NetworkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        cm.registerNetworkCallback(networkChangeFilter,networkStateChecker )








    }



    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

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




    override fun onDestroy() {
        super.onDestroy()

        sp.unregisterOnSharedPreferenceChangeListener(this)
        cm.unregisterNetworkCallback(networkStateChecker)
    }





}





  // val cm = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
  //  val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
  //  val connected = capabilities?.hasCapability(NET_CAPABILITY_INTERNET) == true
    // could filter using .addCapability(int) or .addTransportType(int) on Builder
    //builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)


  //  val networkChangeFilter: NetworkRequest = NetworkRequest.Builder().build()

    //     val xxx =    cm.registerNetworkCallback(networkChangeFilter,NetworkStateChecker())








