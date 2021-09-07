package com.wallsprime.wallpapers.ui.setting

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.*
import androidx.work.*
import com.google.android.material.appbar.MaterialToolbar
import com.wallsprime.wallpapers.R
import com.wallsprime.wallpapers.worker.AutoWallpaperChangerWorker
import java.util.concurrent.TimeUnit

class WallpaperFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {



    private lateinit var navController: NavController
    private lateinit var listener : NavController.OnDestinationChangedListener
    private lateinit var sp: SharedPreferences
    private lateinit var autoWallpaperChanger: SwitchPreferenceCompat
    private lateinit var random: SwitchPreferenceCompat
    private lateinit var favourite: SwitchPreferenceCompat
    private lateinit var category: SwitchPreferenceCompat
    private lateinit var workManager: WorkManager




    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.wallpaper_preferences, rootKey)


         workManager= WorkManager.getInstance(requireContext())
         autoWallpaperChanger = findPreference<SwitchPreferenceCompat>("autowallpaperchanger")!!
         random = findPreference<SwitchPreferenceCompat>("random")!!
         favourite = findPreference<SwitchPreferenceCompat>("favourite")!!
         category = findPreference<SwitchPreferenceCompat>("category")!!



        autoWallpaperChanger?.onPreferenceClickListener= this
        random?.onPreferenceClickListener= this
        favourite?.onPreferenceClickListener= this
        category?.onPreferenceClickListener= this




    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sp = PreferenceManager.getDefaultSharedPreferences(context)




        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbarWallpaperSetting)
        navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)


        listener = NavController.OnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.wallpaperFragment) {

                toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24_explore)

            }

        }
        navController.addOnDestinationChangedListener(listener)


        sp.registerOnSharedPreferenceChangeListener(this)





    }



    override fun onDestroyView() {
        super.onDestroyView()
        navController.removeOnDestinationChangedListener(listener)
        sp.unregisterOnSharedPreferenceChangeListener(this)

    }





    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

      //  Toast.makeText(context, "preference changed", Toast.LENGTH_SHORT).show()
        var data: Data? = null
        val favData = sp.getBoolean("favourite",false)
        val randomData = sp.getBoolean("random",false)
        val categoryData = sp.getBoolean("category",false)
        val categoriesData = sp.getStringSet("categories",HashSet<String>())?.toTypedArray()
        var time = sp.getString("time","15")
        val wifi = sp.getBoolean("wifi",false)
        val charging = sp.getBoolean("charging",false)




        if (favData){

            data = Data.Builder().putString("source","favourite").build()

        }

        if (randomData){

            data = Data.Builder().putString("source","random").build()

        }

        if (categoryData && categoriesData != null){
            data = Data.Builder().putStringArray("category",categoriesData).build()
        }



        if(autoWallpaperChanger.isChecked && (random.isChecked || favourite.isChecked || category.isChecked) && data != null ){


            val networkType:NetworkType = if (wifi){
                 NetworkType.UNMETERED
             } else{
                 NetworkType.METERED
             }

            val constraints = Constraints.Builder()
                .setRequiresCharging(charging)
                .setRequiredNetworkType(networkType)
                .build()


            val changeRequest = PeriodicWorkRequestBuilder<AutoWallpaperChangerWorker>(time!!.toLong(), TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInputData(data)
                .build()


           // workManager= WorkManager.getInstance(requireContext())

            workManager.enqueueUniquePeriodicWork(
                "changeWallpaper",
                ExistingPeriodicWorkPolicy.REPLACE,
                changeRequest
            )


        }


        if (!(autoWallpaperChanger.isChecked) ){

            workManager.cancelUniqueWork("changeWallpaper")
           // workManager.getWorkInfosForUniqueWorkLiveData("changeWallpaper")
        }


    }







    override fun onPreferenceClick(preference: Preference?): Boolean {

       if (preference?.key == "random" && random?.isChecked == true){
           favourite?.isChecked = false
           category?.isChecked = false
       }


        if (preference?.key == "favourite" && favourite?.isChecked == true){
            random?.isChecked = false
            category?.isChecked = false
        }

        if (preference?.key == "category" && category?.isChecked == true){
            random?.isChecked = false
            favourite?.isChecked = false
        }

        return true
    }









}