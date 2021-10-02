package com.wallsprime.wallpapers.ui.explore

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.wallsprime.wallpapers.R
import com.wallsprime.wallpapers.adapters.common.AdapterItemDecorator
import com.wallsprime.wallpapers.adapters.explore.CategoryAdapter
import com.wallsprime.wallpapers.data.viewmodels.UnsplashViewModel
import com.wallsprime.wallpapers.databinding.FragmentCategoryExploreBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CategoryFragmentExploreFragment : Fragment(R.layout.fragment_category_explore) {

    private val viewModelCategoryExploreFragment: UnsplashViewModel by activityViewModels()
    private lateinit var navController: NavController
    private var _exploreBinding: FragmentCategoryExploreBinding? = null
    private val exploreBinding get() = _exploreBinding!!




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _exploreBinding = FragmentCategoryExploreBinding.bind(view)


        val exploreCategoryRecyclerViewAdapter = CategoryAdapter(
            onFavouriteClick = { photoItem ->
                viewModelCategoryExploreFragment.onFavouriteClick(photoItem)
            }
        )
        //set up toolbar
        navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        exploreBinding.apply {
            toolbarCategoryExplore.setupWithNavController(navController, appBarConfiguration)


        // setup RecyclerView

            recyclerviewCategoryExplore.apply {
                layoutManager = GridLayoutManager(context,2)
                setHasFixedSize(true)
                isVerticalScrollBarEnabled = false
                itemAnimator?.changeDuration = 0
                addItemDecoration(AdapterItemDecorator(2,10,true))

            }

        viewModelCategoryExploreFragment.exploreResults?.observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                exploreCategoryRecyclerViewAdapter.submitData(it)

            }
        })

            recyclerviewCategoryExplore.adapter = exploreCategoryRecyclerViewAdapter



            SwipeRefreshLayoutExplore.setOnRefreshListener {
                exploreCategoryRecyclerViewAdapter.refresh()
            }

            retryExplore.setOnClickListener {
                exploreCategoryRecyclerViewAdapter.retry()
            }



            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                exploreCategoryRecyclerViewAdapter.loadStateFlow.collect { loadState ->

                    when (val refresh = loadState.mediator?.refresh) {
                        is LoadState.Loading -> {
                            recyclerviewCategoryExplore.scrollToPosition(0)

                        }
                        is LoadState.NotLoading -> {
                            SwipeRefreshLayoutExplore.isRefreshing = false



                        }
                        is LoadState.Error -> {
                            SwipeRefreshLayoutExplore.isRefreshing = false


                        }
                    }
                }

            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                exploreCategoryRecyclerViewAdapter.loadStateFlow.collect { loadState ->

                    when (val refresh = loadState.mediator?.append) {
                        is LoadState.Loading -> {
                            progressBarExplore.isVisible = true
                            errorExploreLayout.isVisible = false

                        }
                        is LoadState.NotLoading -> {
                            progressBarExplore.isVisible = false
                            errorExploreLayout.isVisible = false

                        }
                        is LoadState.Error -> {
                            progressBarExplore.isVisible = false
                            errorExploreLayout.isVisible = true


                        }

                    }

                }

            }






            if (viewModelCategoryExploreFragment.exploreCurrentPosition != null) {
            viewModelCategoryExploreFragment.exploreCurrentPosition?.let {
                recyclerviewCategoryExplore.scrollToPosition(it)
            }
            viewModelCategoryExploreFragment.exploreCurrentPosition = null
        }


    }


    }


    override fun onDestroyView() {
        super.onDestroyView()

        _exploreBinding = null

    }






}