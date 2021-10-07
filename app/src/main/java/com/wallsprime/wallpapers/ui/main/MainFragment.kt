package com.wallsprime.wallpapers.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.wallsprime.wallpapers.R
import com.wallsprime.wallpapers.adapters.common.TAB_INDEX_ONE
import com.wallsprime.wallpapers.adapters.common.TAB_INDEX_THREE
import com.wallsprime.wallpapers.adapters.common.TAB_INDEX_TWO
import com.wallsprime.wallpapers.adapters.common.ViewPagerAdapterMainFragment
import com.wallsprime.wallpapers.databinding.FragmentMainBinding


class MainFragment : Fragment(R.layout.fragment_main) {


    private var _mainBinding: FragmentMainBinding? = null
    private val mainBinding get() = _mainBinding!!




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _mainBinding = FragmentMainBinding.bind(view)


       mainBinding.apply {

        // set up viewPagerAdapterHomeFragment
           viewPager2FragmentMain.adapter = ViewPagerAdapterMainFragment(this@MainFragment)
           TabLayoutMediator(tabLayoutFragmentMain, viewPager2FragmentMain) { tab, position ->
            // tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)
          }.attach()





           val navController = findNavController()

           toolbarFragmentMain.setOnMenuItemClickListener {

               when (it.itemId) {

                   R.id.settingFragment -> {

                       val direction = MainFragmentDirections.actionMainFragmentToSettingPerference()
                       navController.navigate(direction)
                       true
                   }

                   R.id.autoWallpaperChanger -> {

                       val direction = MainFragmentDirections.actionMainFragmentToWallpaperFragment()
                       navController.navigate(direction)
                       true
                   }

                   R.id.rate -> {

                       val intent = Intent(Intent.ACTION_VIEW)
                       intent.setData(Uri.parse(("market://details?id=" + context?.getPackageName())))
                       try {
                           startActivity (intent)
                       }
                       catch(e:Exception){
                       }
                       true
                   }

                   R.id.about ->   {
                       val url = "https://sites.google.com/view/techsneak-labs/about"
                       val intent = Intent(Intent.ACTION_VIEW)
                       intent.setData(Uri.parse(url))
                       startActivity (intent)
                       true
                   }

                   R.id.privacy ->  {
                       val url = "https://sites.google.com/view/techsneak-labs/privacy-policy"
                       val intent = Intent(Intent.ACTION_VIEW)
                       intent.setData(Uri.parse(url))
                       startActivity (intent)
                       true
                   }

                   R.id.disclaimer ->  {
                       val url = "https://sites.google.com/view/techsneak-labs/disclaimer"
                       val intent = Intent(Intent.ACTION_VIEW)
                       intent.setData(Uri.parse(url))
                       startActivity (intent)
                       true
                   }

                   else -> false
               }
           }



       }


 }




    override fun onDestroyView() {
        super.onDestroyView()
        _mainBinding = null

    }






    private fun getTabTitle(position: Int): String? {
        return when (position) {
            TAB_INDEX_ONE -> "Home"
            TAB_INDEX_TWO -> "Explore"
            TAB_INDEX_THREE -> "Favourite"

            else -> null
        }
    }




}