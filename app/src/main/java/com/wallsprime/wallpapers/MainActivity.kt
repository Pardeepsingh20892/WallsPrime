package com.wallsprime.wallpapers







import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.preference.PreferenceManager
import com.wallsprime.wallpapers.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



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



    override fun onResume() {
        super.onResume()

    }


    override fun onDestroy() {
        super.onDestroy()

    }




}












