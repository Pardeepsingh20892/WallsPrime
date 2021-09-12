package com.wallsprime.wallpapers.worker

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.preference.PreferenceManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.wallsprime.wallpapers.data.database.UnsplashPhotoDatabase
import com.wallsprime.wallpapers.di.AppModule.provideRetrofit
import com.wallsprime.wallpapers.di.AppModule.provideUnsplashApi
import kotlinx.coroutines.*
import kotlin.random.Random

/*
@HiltWorker
@AssistedInject
@Assisted
*/


class AutoWallpaperChangerWorker(
      context: Context,
      workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private var myContext =  context
    private var source : String? = getInputData().getString("source")
    private var category: Array<String>? = getInputData().getStringArray("category")
    private var sp = PreferenceManager.getDefaultSharedPreferences(context).getString("screentoapply","both")

    override suspend fun doWork(): Result = coroutineScope  {


        try {


           var getDB =  UnsplashPhotoDatabase.getInstance(myContext)
           var imgUrl: String? = null


           when(source){

               "random"   ->{

                   var getApi = provideUnsplashApi(provideRetrofit())
                   var getPage = Random.nextInt(1,50)
                   val response = getApi.getPhotos(getPage , 10)
                   val serverResults = response.body()!!
                   var getNum = Random.nextInt(0,serverResults.size)
                   imgUrl =   serverResults[getNum].urls.regular
                   getGlide(imgUrl)



               }
               "favourite"   ->{

                   var getApi = provideUnsplashApi(provideRetrofit())
                   var getFav = getDB.favouriteDao().getFavouritePhotos()
                   var randomNo = Random.nextInt(0,getFav.size)
                   var getId = getFav[randomNo].id
                   val response = getApi.getRandomPhotos(getId)
                   val serverResults = response.body()!!
                   imgUrl =   serverResults.urls.regular
                   getGlide(imgUrl)


               }


           }



            if(category != null) {


                    var getRandomNo = Random.nextInt(0, category!!.size)
                    var getId = category!![getRandomNo]
                    var getPage = Random.nextInt(1, 6)
                    var getApi = provideUnsplashApi(provideRetrofit())
                    val responseCategory = getApi.getCollectionPhotos(getId, getPage, 10)
                    val resultCategory = responseCategory.body()!!
                    var getNumCategory = Random.nextInt(0, resultCategory.size)
                    imgUrl = resultCategory.get(getNumCategory).urls.regular
                    getGlide(imgUrl)

            }



            Result.success()

        } catch (ex: Exception) {
            Log.e("pardeep", "Error loading wallpaper", ex)

            Result.failure()
        }



    }




    private fun getGlide(imgUrl: String){


        Glide.with(myContext)
            .asBitmap()
            .load(imgUrl)
            .into(object : CustomTarget<Bitmap>() {



                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                         CoroutineScope(Dispatchers.Default).launch {
                          setWallpaper(resource,sp!!)
                         }
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }






    private fun setWallpaper(resource: Bitmap, code:String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && code =="home"){

        WallpaperManager.getInstance(myContext).setBitmap(resource,null,false,
             WallpaperManager.FLAG_SYSTEM)

        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && code =="lock"){

            WallpaperManager.getInstance(myContext).setBitmap(resource,null,false,
                WallpaperManager.FLAG_LOCK)
        }

        else{

        WallpaperManager.getInstance(myContext).setBitmap(resource)

        }


    }




}