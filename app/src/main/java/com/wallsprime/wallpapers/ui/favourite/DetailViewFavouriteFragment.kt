package com.wallsprime.wallpapers.ui.favourite

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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.wallsprime.wallpapers.R
import com.wallsprime.wallpapers.adapters.favourite.FavouriteDetailViewAdapter
import com.wallsprime.wallpapers.data.viewmodels.UnsplashViewModel
import com.wallsprime.wallpapers.databinding.FragmentDetailViewFavouriteBinding
import com.wallsprime.wallpapers.utils.DownloadResult
import com.wallsprime.wallpapers.utils.downloadFile
import com.wallsprime.wallpapers.utils.shareOrSetWallpaper
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.*
import io.ktor.client.engine.android.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class DetailViewFavouriteFragment : Fragment(R.layout.fragment_detail_view_favourite) {


    private val viewModelDetailViewFavouriteFragment: UnsplashViewModel by activityViewModels()

    private lateinit var navController: NavController
    private lateinit var listener : NavController.OnDestinationChangedListener
    private var _favouriteBinding: FragmentDetailViewFavouriteBinding? = null
    private val favouriteBinding get() = _favouriteBinding!!
    private var getImageItem: String? = null
    private var itemCode: Int? = null
    private var userProfile: String? = null
    private lateinit var job : Job



    override fun onResume() {
        super.onResume()
        /*

       requireActivity().window.decorView.windowInsetsController!!.hide(
           WindowInsets.Type.statusBars()
       )
       */

        requireActivity().window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _favouriteBinding = FragmentDetailViewFavouriteBinding.bind(view)



        // get item position argument
        val argsFav = arguments?.let {
            DetailViewFavouriteFragmentArgs.fromBundle(it).favouriteItemPosition
        }

        val recyclerViewAdapterDetailViewFavouriteFragment = FavouriteDetailViewAdapter(
            onFavouriteClick = { photoItem ->
                viewModelDetailViewFavouriteFragment.onFavouriteClick(photoItem)
            },
            getItemUrl = {itemUrl,code ->
                requestPermissionAndSave(itemUrl,requireContext(),code)

            },
            itemPosition = {

                viewModelDetailViewFavouriteFragment.favouriteCurrentPosition=it-1

            },
            userProfile = {
                userProfile = it

            }

        )

          navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)


        favouriteBinding.apply {
        // set up toolbar
            toolbarFavouriteDetailView.setupWithNavController( navController, appBarConfiguration)
            toolbarFavouriteDetailView.setOnMenuItemClickListener {

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


        listener = NavController.OnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.detailViewFavouriteFragment){
                toolbarFavouriteDetailView.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
            }
        }

        navController.addOnDestinationChangedListener(listener)

        // set up RecyclerView
        val layoutManager = GridLayoutManager(context,1)
            recyclerViewFavouriteDetailView.layoutManager = layoutManager
            recyclerViewFavouriteDetailView.hasFixedSize()
            recyclerViewFavouriteDetailView.itemAnimator?.changeDuration = 0
            recyclerViewFavouriteDetailView.isVerticalScrollBarEnabled = false
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerViewFavouriteDetailView)




        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModelDetailViewFavouriteFragment.favouriteResults.collectLatest {

                var favourite = it ?: return@collectLatest
                recyclerViewAdapterDetailViewFavouriteFragment.submitList(favourite)
                noFavAdded.isVisible = favourite.isEmpty()
                recyclerViewFavouriteDetailView.isVisible = favourite.isNotEmpty()
            }

        }

            recyclerViewFavouriteDetailView.adapter = recyclerViewAdapterDetailViewFavouriteFragment
            recyclerViewFavouriteDetailView.scrollToPosition(argsFav!!)


        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        navController.removeOnDestinationChangedListener(listener)
        _favouriteBinding = null
        requireActivity().window?.decorView?.systemUiVisibility = 0


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

        favouriteBinding.progressBar.progress = 0
        favouriteBinding.textViewProgress.text = "0%"
        favouriteBinding.downloading.isVisible = true

        if (this::job.isInitialized){
            job.cancel()
        }

        val ktor = HttpClient(Android)

       // viewModelDetailViewFragment.setDownloading(true)
        job = lifecycleScope.launch(Dispatchers.IO) {
        context.contentResolver.openOutputStream(file)?.let { outputStream ->

                ktor.downloadFile(outputStream, url).collect {

                    withContext(Dispatchers.Main) {
                        when (it) {
                            is DownloadResult.Success -> {
                               // viewModelDetailViewFragment.setDownloading(false)
                                favouriteBinding.downloading.isVisible = false
                                favouriteBinding.progressBar.progress = 0
                                favouriteBinding.textViewProgress.text = "0%"

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
                               // viewModelDetailViewFragment.setDownloading(false)
                                favouriteBinding.downloading.isVisible = false
                                favouriteBinding.progressBar.progress = 0
                                favouriteBinding.textViewProgress.text = "0%"

                                Toast.makeText(
                                    context,
                                    "Error",
                                    Toast.LENGTH_LONG
                                ).show()


                               // resolver.delete(file,null)

                            }

                            is DownloadResult.Progress -> {
                                var progress = it.progress
                                favouriteBinding.progressBar.progress = progress
                                favouriteBinding.textViewProgress.text = "$progress%"

                            }
                        }
                    }


                }


            }
        }
    }













}