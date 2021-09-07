package com.wallsprime.wallpapers.data.favourite

import com.wallsprime.wallpapers.data.common.UnsplashPhoto
import com.wallsprime.wallpapers.data.database.UnsplashPhotoDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavouriteRepository @Inject constructor(
    private val unsplashPhotoDb: UnsplashPhotoDatabase
 ){



    private val favouriteDao = unsplashPhotoDb.favouriteDao()

    fun  getUnsplashFavouritePhotos(): Flow<List<UnsplashPhoto>> = favouriteDao.getUnsplashFavouritePhotos()


}