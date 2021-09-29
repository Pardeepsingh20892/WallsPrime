package com.wallsprime.wallpapers.ui.home

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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.wallsprime.wallpapers.adapters.home.HomeDetailViewAdapter
import com.wallsprime.wallpapers.data.viewmodels.UnsplashViewModel
import com.wallsprime.wallpapers.databinding.FragmentDetailViewHomeBinding
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

@AndroidEntryPoint
class DetailViewFragmentHome : Fragment(R.layout.fragment_detail_view_home) {

    private val viewModelDetailViewFragment: UnsplashViewModel by activityViewModels()
    private lateinit var navController: NavController
    private var _homeBinding: FragmentDetailViewHomeBinding? = null
    private val homeBinding get() = _homeBinding!!
    private var getImageItem: String? = null
    private var itemCode: Int? = null
    private var userProfile: String? = null
    private lateinit var job : Job





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inflate the layout for this fragment
        _homeBinding = FragmentDetailViewHomeBinding.bind(view)



        // get item position argument
         val args = arguments?.let {
            DetailViewFragmentHomeArgs.fromBundle(it).itemPosition
         }




        navController = findNavController()
        val recyclerViewAdapterDetailViewFragment = HomeDetailViewAdapter(
            onFavouriteClick = { photoItem ->
                viewModelDetailViewFragment.onFavouriteClick(photoItem)

            },
            getItemUrl = {itemUrl,code ->
                requestPermissionAndSave(itemUrl,requireContext(),code)

            },
            itemPosition = {

                viewModelDetailViewFragment.homeCurrentPosition=it-1

            },
            userProfile = {
                userProfile = it

            }

        )




        homeBinding.apply {

            // set up toolbar
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            toolbarFragmentDetailView.setupWithNavController( navController, appBarConfiguration)
            toolbarFragmentDetailView.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            toolbarFragmentDetailView.setOnMenuItemClickListener {

                    when (it.itemId) {
                        R.id.Info -> {
                            val uri = Uri.parse(userProfile)
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                            true
                        }
                        else -> false
                    }
                }




            // set up RecyclerView
            recyclerViewFragmentDetailView.apply {
            layoutManager = GridLayoutManager(context,1)
            hasFixedSize()
            itemAnimator?.changeDuration = 0
            isVerticalScrollBarEnabled = false
            PagerSnapHelper().attachToRecyclerView(this)

            }

           // submit data to paging adapter
            viewModelDetailViewFragment.homeResults.observe(viewLifecycleOwner,{
                lifecycleScope.launch {
                    recyclerViewAdapterDetailViewFragment.submitData(it)
                }
            })

            recyclerViewFragmentDetailView.adapter = recyclerViewAdapterDetailViewFragment
            recyclerViewFragmentDetailView.scrollToPosition(args!!)




            retryHome.setOnClickListener {
                recyclerViewAdapterDetailViewFragment.retry()
            }


            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                recyclerViewAdapterDetailViewFragment.loadStateFlow.collect { loadState ->

                    when (val refresh = loadState.mediator?.append) {
                        is LoadState.Loading -> {
                            loadingAnimation.isVisible = true
                            errorHomeLayout.isVisible = false

                        }
                        is LoadState.NotLoading -> {
                            loadingAnimation.isVisible = false
                            errorHomeLayout.isVisible = false

                        }
                        is LoadState.Error -> {
                            loadingAnimation.isVisible = false
                            errorHomeLayout.isVisible= true


                        }

                    }

                }

            }



        }



    }














    private val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                createFileAndDownload(getImageItem!!, requireContext(),itemCode!!)

            }
        }








   private fun requestPermissionAndSave(url: String, context: Context,code: Int) {
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
                     // put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/"+"WallsPrime")

                  }

                  val imageContentUri: Uri? = resolver.insert(imageCollection, imageDetails)

                       downloadFile(context,url, code,imageContentUri!!,resolver,imageDetails)


              }





    private fun downloadFile(context: Context, url: String,code: Int, file: Uri,resolver: ContentResolver,imageDetails: ContentValues) {

        homeBinding.progressBar.progress = 0
        homeBinding.textViewProgress.text = "0%"
        homeBinding.downloading.isVisible = true

       if (this::job.isInitialized){
           job.cancel()
       }

        val ktor = HttpClient(Android)


        job =  lifecycleScope.launch(Dispatchers.IO) {
        context.contentResolver.openOutputStream(file)?.let { outputStream ->
                ktor.downloadFile(outputStream, url).collect {

                    withContext(Dispatchers.Main) {
                        when (it) {
                            is DownloadResult.Success -> {
                                homeBinding.downloading.isVisible = false
                                homeBinding.progressBar.progress = 0
                                homeBinding.textViewProgress.text = "0%"

                                if (code == 1){
                                Toast.makeText(
                                    context,
                                    "Download Complete",
                                    Toast.LENGTH_LONG
                                ).show()


                                }

                                resolver.update(file, imageDetails, null, null)
                                shareOrSetWallpaper(file, context, code)


                            }

                            is DownloadResult.Error -> {
                                homeBinding.downloading.isVisible = false
                                homeBinding.progressBar.progress = 0
                                homeBinding.textViewProgress.text = "0%"

                                Toast.makeText(
                                    context,
                                    "Error",
                                    Toast.LENGTH_LONG
                                ).show()


                              //  resolver.delete(file,null)

                            }

                            is DownloadResult.Progress -> {
                                var progress = it.progress
                                homeBinding.progressBar.progress = progress
                                homeBinding.textViewProgress.text = "$progress%"

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
        // navController = null
         showSystemUI()
        _homeBinding = null

    }



    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        WindowInsetsControllerCompat(requireActivity().window, homeBinding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.statusBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)
        WindowInsetsControllerCompat(requireActivity().window, homeBinding.root).show(WindowInsetsCompat.Type.statusBars())
    }



}