package com.wallsprime.wallpapers.data.explore

import androidx.paging.PagingSource
import androidx.room.*
import com.wallsprime.wallpapers.data.common.UnsplashPhoto


@Dao
interface   ExploreDao {


    // categories
    @Query("DELETE FROM explore_category")
    suspend fun deleteUnsplashPhotosExplore()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUnsplashCategoryPhotos(photos: List<ExploreCategory>)

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM explore_category INNER JOIN unsplash_photo ON PhotoId = id WHERE collectionId = :collectionId")
    fun getExploreCategoryPhotosPaged(collectionId: String): PagingSource<Int, UnsplashPhoto>







    // explore
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnsplashCategory(photos: List<UnsplashExplore>)

    @Query("SELECT * FROM unsplash_explore")
    suspend fun  getUnsplashCategory(): List<UnsplashExplore>




}