package com.wallsprime.wallpapers.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import io.ktor.client.*
import io.ktor.client.engine.android.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*




 fun downloadFilexxx(
    context: Context,
    url: String,
    code: Int,
    file: Uri,
    resolver: ContentResolver,
    imageDetails: ContentValues,
    homeBinding: ViewBinding,
    lifecycleOwner: LifecycleOwner

) {

    homeBinding.progressBar.progress = 0
    homeBinding.textViewProgress.text = "0%"
    homeBinding.downloading.isVisible = true

  //  if (this::job.isInitialized){
   //     job.cancel()
  //  }


   var job =  lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
        context.contentResolver.openOutputStream(file)?.let { outputStream ->
            HttpClient(Android).downloadFile(outputStream, url).collect {

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

*/
