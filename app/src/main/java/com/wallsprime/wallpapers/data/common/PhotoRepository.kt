package com.wallsprime.wallpapers.data.common

import com.wallsprime.wallpapers.api.Api
import com.wallsprime.wallpapers.data.database.UnsplashPhotoDatabase
import javax.inject.Inject




class PhotoRepository @Inject constructor(
    private val unsplashApi: Api,
    private val unsplashPhotoDb: UnsplashPhotoDatabase
){

    private val photoDao = unsplashPhotoDb.photoDao()

    suspend fun updateUnsplashPhoto(photos: UnsplashPhoto) = photoDao.updateUnsplashPhotos(photos)
    suspend fun download(id: String) = unsplashApi.download(id)


}