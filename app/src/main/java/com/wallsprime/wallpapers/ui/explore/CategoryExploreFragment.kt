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
    private lateinit var listener : NavController.OnDestinationChangedListener
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



        listener = NavController.OnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.categoryFragmentExploreFragment) {

                toolbarCategoryExplore.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24_explore)

            }

        }
        navController.addOnDestinationChangedListener(listener)



        // setup RecyclerView
        val layoutManager = GridLayoutManager(context, 2)
            recyclerviewCategoryExplore.layoutManager = layoutManager
            recyclerviewCategoryExplore.setHasFixedSize(true)
            recyclerviewCategoryExplore.addItemDecoration(AdapterItemDecorator(2,10,true))
            recyclerviewCategoryExplore.isVerticalScrollBarEnabled = false
            recyclerviewCategoryExplore.itemAnimator?.changeDuration = 0



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
                           // SwipeRefreshLayoutExplore.isRefreshing = true
                          //  errorExplore.isVisible = false

                        }
                        is LoadState.NotLoading -> {
                            SwipeRefreshLayoutExplore.isRefreshing = false
                           // errorExplore.isVisible = false

                        }
                        is LoadState.Error -> {
                            SwipeRefreshLayoutExplore.isRefreshing = false
                          //  errorExplore.isVisible = true

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

        navController.removeOnDestinationChangedListener(listener)
        _exploreBinding = null

    }






}