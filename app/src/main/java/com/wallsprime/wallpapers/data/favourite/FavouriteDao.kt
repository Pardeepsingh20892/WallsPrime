package com.wallsprime.wallpapers.data.favourite

import androidx.room.*
import com.wallsprime.wallpapers.data.common.UnsplashPhoto
import kotlinx.coroutines.flow.Flow



@Dao
interface FavouriteDao{



    @Query("SELECT * FROM unsplash_photo WHERE favourite = 1")
    fun  getUnsplashFavouritePhotos(): Flow<List<UnsplashPhoto>>


    @Query("SELECT * FROM unsplash_photo WHERE favourite = 1")
    fun  getFavouritePhotos():List<UnsplashPhoto>

}