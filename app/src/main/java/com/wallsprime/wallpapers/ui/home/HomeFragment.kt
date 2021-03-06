package com.wallsprime.wallpapers.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wallsprime.wallpapers.R
import com.wallsprime.wallpapers.adapters.common.AdapterItemDecorator
import com.wallsprime.wallpapers.adapters.home.HomeAdapter
import com.wallsprime.wallpapers.data.viewmodels.UnsplashViewModel
import com.wallsprime.wallpapers.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch



class HomeFragment : Fragment(R.layout.fragment_home) {


    private val viewModelHomeFragment: UnsplashViewModel by activityViewModels()
    private var _homeBinding: FragmentHomeBinding? = null
    private val homeBinding get() = _homeBinding!!



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inflate the layout for this fragment
        _homeBinding = FragmentHomeBinding.bind(view)



        val unsplashPhotoPagingAdapter = HomeAdapter(onFavouriteClick = { photoItem ->
            viewModelHomeFragment.onFavouriteClick(photoItem)
        })

        unsplashPhotoPagingAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY



        homeBinding.apply {

            // setup RecyclerView
            recyclerviewFragmentHome.apply {
            layoutManager = GridLayoutManager(context,2)
            setHasFixedSize(true)
            isVerticalScrollBarEnabled = false
            itemAnimator?.changeDuration = 0
            addItemDecoration(AdapterItemDecorator(2, 10))

            }


            viewModelHomeFragment.homeResults.observe(viewLifecycleOwner,{
                lifecycleScope.launch {
                    unsplashPhotoPagingAdapter.submitData(it)
                }
            })

            recyclerviewFragmentHome.adapter =   unsplashPhotoPagingAdapter



            SwipeRefreshLayoutHome.setOnRefreshListener {
                unsplashPhotoPagingAdapter.refresh()
               // recyclerviewFragmentHome.scrollToPosition(0)
            }

            retryHome.setOnClickListener {
                unsplashPhotoPagingAdapter.retry()
            }




            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                unsplashPhotoPagingAdapter.loadStateFlow.collect { loadState ->

                    when (val refresh = loadState.mediator?.refresh) {
                        is LoadState.Loading -> {
                            recyclerviewFragmentHome.scrollToPosition(0)


                        }
                        is LoadState.NotLoading -> {
                            SwipeRefreshLayoutHome.isRefreshing = false



                        }
                        is LoadState.Error -> {
                            SwipeRefreshLayoutHome.isRefreshing = false


                        }

                    }
                }

            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                unsplashPhotoPagingAdapter.loadStateFlow.collect { loadState ->

                    when (val refresh = loadState.mediator?.append) {
                        is LoadState.Loading -> {
                            loadingAnimation.isVisible = true
                            errorHome.isVisible = false
                            retryHome.isVisible = false

                        }
                        is LoadState.NotLoading -> {
                            loadingAnimation.isVisible = false
                            errorHome.isVisible = false
                            retryHome.isVisible = false

                        }
                        is LoadState.Error -> {
                            loadingAnimation.isVisible = false
                            errorHome.isVisible= true
                            retryHome.isVisible = true


                        }

                    }

                }

            }



            if (viewModelHomeFragment.homeCurrentPosition != null) {
                viewModelHomeFragment.homeCurrentPosition?.let { recyclerviewFragmentHome.scrollToPosition(it) }
                viewModelHomeFragment.homeCurrentPosition = null
            }


        }



    }



    override fun onDestroyView() {
        super.onDestroyView()
        _homeBinding = null

    }



}


