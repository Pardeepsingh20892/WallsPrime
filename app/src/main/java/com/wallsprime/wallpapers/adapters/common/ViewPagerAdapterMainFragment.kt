package com.wallsprime.wallpapers.adapters.common

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wallsprime.wallpapers.ui.explore.ExploreFragment
import com.wallsprime.wallpapers.ui.favourite.FavouriteFragment
import com.wallsprime.wallpapers.ui.home.HomeFragment


const val TAB_INDEX_ONE = 0
const val TAB_INDEX_TWO = 1
const val TAB_INDEX_THREE = 2


class ViewPagerAdapterMainFragment(fragment: Fragment) : FragmentStateAdapter(fragment) {


    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
             TAB_INDEX_ONE to { HomeFragment() },
            TAB_INDEX_TWO to { ExploreFragment() },
            TAB_INDEX_THREE to { FavouriteFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}