package com.wallsprime.wallpapers.data.home

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wallsprime.wallpapers.api.Api
import com.wallsprime.wallpapers.data.common.UnsplashPhoto
import com.wallsprime.wallpapers.data.database.UnsplashPhotoDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val unsplashApi: Api,
    private val unsplashPhotoDb: UnsplashPhotoDatabase
) {

    private val unsplashPhotoDao = unsplashPhotoDb.homeDao()



    fun getPhotoResultsPaged(
        collection: String,
        refreshOnInit: Boolean
    ): Flow<PagingData<UnsplashPhoto>> =
        Pager(
            config = PagingConfig(pageSize = 50),
            remoteMediator = HomeRemoteMediator(collection, unsplashApi, unsplashPhotoDb, refreshOnInit),
            pagingSourceFactory = { unsplashPhotoDao.getUnsplashPhotoPaged() }
        ).flow

}