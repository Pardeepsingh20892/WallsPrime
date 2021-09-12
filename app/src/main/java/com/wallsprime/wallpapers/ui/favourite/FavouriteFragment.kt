package com.wallsprime.wallpapers.ui.favourite

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wallsprime.wallpapers.R
import com.wallsprime.wallpapers.adapters.common.AdapterItemDecorator
import com.wallsprime.wallpapers.adapters.favourite.FavouriteAdapter
import com.wallsprime.wallpapers.data.viewmodels.UnsplashViewModel
import com.wallsprime.wallpapers.databinding.FragmentFavouriteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FavouriteFragment : Fragment(R.layout.fragment_favourite) {


  //  private lateinit var navController: NavController

    private val viewModelFavouriteFragment: UnsplashViewModel by activityViewModels()
    private var _favouriteBinding: FragmentFavouriteBinding? = null
    private val favouriteBinding get() = _favouriteBinding!!





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _favouriteBinding = FragmentFavouriteBinding.bind(view)

        val favouriteRecyclerViewAdapter = FavouriteAdapter(onFavouriteClick = { photoItem ->
            viewModelFavouriteFragment.onFavouriteClick(photoItem)
        })

        favouriteRecyclerViewAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY




        favouriteBinding.apply {


            val width = Resources.getSystem().getDisplayMetrics().widthPixels
            val span: Int = width/500

        // setup RecyclerView
        val layoutManager = GridLayoutManager(context,2)
            recyclerviewFragmentFavourite.layoutManager = layoutManager
            recyclerviewFragmentFavourite.setHasFixedSize(true)
            recyclerviewFragmentFavourite.addItemDecoration(AdapterItemDecorator(2,10,true))
            recyclerviewFragmentFavourite.isVerticalScrollBarEnabled = false
            recyclerviewFragmentFavourite.itemAnimator?.changeDuration = 0




        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModelFavouriteFragment.favouriteResults.collectLatest {

                var favourite = it ?: return@collectLatest
                favouriteRecyclerViewAdapter.submitList(favourite)
                animationViewNoFav.isVisible = favourite.isEmpty()
                textViewNoFav.isVisible = favourite.isEmpty()
                recyclerviewFragmentFavourite.isVisible = favourite.isNotEmpty()

            }

        }

            recyclerviewFragmentFavourite.adapter =  favouriteRecyclerViewAdapter


        if (viewModelFavouriteFragment.favouriteCurrentPosition != null)
        {
            viewModelFavouriteFragment.favouriteCurrentPosition?.let { recyclerviewFragmentFavourite.scrollToPosition(it) }
            viewModelFavouriteFragment.favouriteCurrentPosition=null

        }

    }



    }


    override fun onDestroyView() {
        super.onDestroyView()
        _favouriteBinding = null
    }




}