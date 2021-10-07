package com.wallsprime.wallpapers.ui.explore

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowInsets
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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
import androidx.recyclerview.widget.PagerSnapHelper
import com.wallsprime.wallpapers.R
import com.wallsprime.wallpapers.adapters.explore.CategoryDetailViewAdapter
import com.wallsprime.wallpapers.data.viewmodels.UnsplashViewModel
import com.wallsprime.wallpapers.databinding.FragmentDetailViewExploreBinding
import com.wallsprime.wallpapers.utils.DownloadResult
import com.wallsprime.wallpapers.utils.downloadFile
import com.wallsprime.wallpapers.utils.shareOrSetWallpaper
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.*
import io.ktor.client.engine.android.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class DetailViewCategoryExploreFragment : Fragment(R.layout.fragment_detail_view_explore) {

    private val viewModelDetailViewCategoryExploreFragment: UnsplashViewModel by activityViewModels()


    private lateinit var navController: NavController
    private var _exploreBinding: FragmentDetailViewExploreBinding? = null
    private val exploreBinding get() = _exploreBinding!!
    private var getImageItem: String? = null
    private var itemCode: Int? = null
    private var userProfile: String? = null
    private lateinit var job : Job







    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _exploreBinding = FragmentDetailViewExploreBinding.bind(view)



        // get item position argument
        val exploreArgs = arguments?.let {
            DetailViewCategoryExploreFragmentArgs.fromBundle(it).itemPositionExplore
        }

        val exploreCategoryDetailViewAdapter = CategoryDetailViewAdapter(
            onFavouriteClick = { photoItem ->
                viewModelDetailViewCategoryExploreFragment.onFavouriteClick(photoItem)
            },
            getItemUrl = {itemUrl,code ->
                requestPermissionAndSave(itemUrl,requireContext(),code)
            },
            itemPosition = {

                viewModelDetailViewCategoryExploreFragment.exploreCurrentPosition = it-1

            },
            userProfile = {
                userProfile = it

            }
        )

        navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)


        exploreBinding.apply {

        // set up toolbar
        toolbarFragmentCategoryDetailView.setupWithNavController( navController, appBarConfiguration)
        toolbarFragmentCategoryDetailView.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbarFragmentCategoryDetailView.setOnMenuItemClickListener {

                when (it.itemId) {
                    R.id.Info -> {
                        val uri = Uri.parse(userProfile )
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)

                        true
                    }

                    else -> false
                }
            }




            // set up RecyclerView
            recyclerViewFragmentCategoryDetailView.apply {
                layoutManager = GridLayoutManager(context,1)
                hasFixedSize()
                itemAnimator?.changeDuration = 0
                isVerticalScrollBarEnabled = false
                PagerSnapHelper().attachToRecyclerView(this)

            }




        viewModelDetailViewCategoryExploreFragment.exploreResults?.observe(viewLifecycleOwner,{
            lifecycleScope.launch {
                exploreCategoryDetailViewAdapter.submitData(it)
            }
        })



            recyclerViewFragmentCategoryDetailView.adapter = exploreCategoryDetailViewAdapter
            recyclerViewFragmentCategoryDetailView.scrollToPosition(exploreArgs!!)



            retryCategory.setOnClickListener {
                exploreCategoryDetailViewAdapter.retry()
            }




            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                exploreCategoryDetailViewAdapter.loadStateFlow.collect { loadState ->

                    when (val refresh = loadState.mediator?.append) {
                        is LoadState.Loading -> {
                            loadingAnimationCategory.isVisible = true
                            errorCategoryLayout.isVisible = false

                        }
                        is LoadState.NotLoading -> {
                            loadingAnimationCategory.isVisible = false
                            errorCategoryLayout.isVisible = false

                        }
                        is LoadState.Error -> {
                            loadingAnimationCategory.isVisible = false
                            errorCategoryLayout.isVisible= true


                        }

                    }

                }

            }



        }



    }






    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {

                createFileAndDownload(getImageItem!!, requireContext(),itemCode!!)

            }
        }

    private fun requestPermissionAndSave(url: String, context: Context, code: Int) {
        getImageItem = url
        itemCode = code

        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ->{

                createFileAndDownload(url, context,code)

            }

            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                requestPermissions.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)   }

            else -> requestPermissions.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }


    }







    private fun   createFileAndDownload(url: String, context: Context, code: Int){

        val resolver = context.contentResolver

        // Find all image files on the primary external storage device.
        val imageCollection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY
                )
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }


        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/*")

        }

        val imageContentUri: Uri? = resolver.insert(imageCollection, imageDetails)

        downloadFile(context,url, code,imageContentUri!!,resolver,imageDetails)


    }





    private fun downloadFile(context: Context, url: String, code: Int, file: Uri, resolver: ContentResolver, imageDetails: ContentValues) {

        exploreBinding.progressBar.progress = 0
        exploreBinding.textViewProgress.text = "0%"
        exploreBinding.downloading.isVisible = true

        if (this::job.isInitialized){
            job.cancel()
        }

        val ktor = HttpClient(Android)


        job = lifecycleScope.launch(Dispatchers.IO) {
        context.contentResolver.openOutputStream(file)?.let { outputStream ->
                ktor.downloadFile(outputStream, url).collect {

                    withContext(Dispatchers.Main) {
                        when (it) {
                            is DownloadResult.Success -> {
                                exploreBinding .downloading.isVisible = false
                                exploreBinding.progressBar.progress = 0
                                exploreBinding.textViewProgress.text = "0%"

                                if (code == 1){
                                    Toast.makeText(
                                        context,
                                        "Download Complete",
                                        Toast.LENGTH_LONG
                                    ).show()



                                }

                                resolver.update(file, imageDetails, null, null)
                                // MediaScannerConnection.scanFile(context, arrayOf(file.path),null,null)
                                shareOrSetWallpaper(file, context, code)


                            }

                            is DownloadResult.Error -> {
                                exploreBinding .downloading.isVisible = false
                                exploreBinding.progressBar.progress = 0
                                exploreBinding.textViewProgress.text = "0%"

                                Toast.makeText(
                                    context,
                                    "Error",
                                    Toast.LENGTH_LONG
                                ).show()


                              //  resolver.delete(file,null)

                            }

                            is DownloadResult.Progress -> {
                                var progress = it.progress
                                exploreBinding .progressBar.progress = progress
                                exploreBinding .textViewProgress.text = "$progress%"

                            }
                        }
                    }


                }


            }
        }
    }




    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }




    override fun onDestroyView() {
        super.onDestroyView()
        showSystemUI()
        _exploreBinding = null



    }









    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)

        exploreBinding.toolbarFragmentCategoryDetailView.setOnApplyWindowInsetsListener { view, windowInsets ->
            val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams

            val insets = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                windowInsets.getInsets(WindowInsets.Type.statusBars()).top
            } else {
                windowInsets.systemWindowInsetTop
            }

            layoutParams.topMargin = insets

            windowInsets
        }


    }

    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)

    }





}