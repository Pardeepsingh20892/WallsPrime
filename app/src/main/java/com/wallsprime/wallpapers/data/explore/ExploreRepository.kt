package com.wallsprime.wallpapers.data.explore

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wallsprime.wallpapers.api.Api
import com.wallsprime.wallpapers.data.database.UnsplashPhotoDatabase
import com.wallsprime.wallpapers.data.common.UnsplashPhoto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExploreRepository @Inject constructor(
    private val unsplashApi: Api,
    private val unsplashPhotoDb: UnsplashPhotoDatabase
) {


    private val unsplashCategoryDao = unsplashPhotoDb.exploreDao()

    fun getExploreResultsPaged(
        collectionId: String,
        refreshOnInit: Boolean
    ): Flow<PagingData<UnsplashPhoto>> =
        Pager(
            config = PagingConfig(pageSize = 50),
            remoteMediator = ExploreRemoteMediator(collectionId, unsplashApi, unsplashPhotoDb, refreshOnInit),
            pagingSourceFactory = { unsplashCategoryDao.getExploreCategoryPhotosPaged(collectionId) }
        ).flow





    suspend  fun  getCategory(): List<UnsplashExplore> = unsplashCategoryDao.getUnsplashCategory()




}