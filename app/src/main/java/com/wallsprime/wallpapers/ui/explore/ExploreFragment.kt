package com.wallsprime.wallpapers.ui.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.wallsprime.wallpapers.R
import com.wallsprime.wallpapers.adapters.common.AdapterItemDecorator
import com.wallsprime.wallpapers.adapters.explore.ExploreAdapter
import com.wallsprime.wallpapers.data.viewmodels.UnsplashViewModel
import com.wallsprime.wallpapers.databinding.FragmentExploreBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment(R.layout.fragment_explore) {



    private val viewModelExploreFragment: UnsplashViewModel by activityViewModels()
    private var _exploreBinding: FragmentExploreBinding? = null
    private val exploreBinding get() = _exploreBinding!!




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _exploreBinding = FragmentExploreBinding.bind(view)





        exploreBinding.apply {



            // set up recyclerView
            RecyclerViewExploreFragment.apply {
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
            addItemDecoration(AdapterItemDecorator(1,20,true))
            isVerticalScrollBarEnabled = false

            }

          lifecycleScope.launchWhenStarted {
              val exploreFragmentRecyclerViewAdapter = ExploreAdapter(viewModelExploreFragment.getPhotoCategory(),
              getPositionId = {
                  viewModelExploreFragment.currentCollectionId = it
                  viewModelExploreFragment.setExploreResult()
              })
              RecyclerViewExploreFragment.adapter = exploreFragmentRecyclerViewAdapter
          }



        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _exploreBinding = null
    }







}