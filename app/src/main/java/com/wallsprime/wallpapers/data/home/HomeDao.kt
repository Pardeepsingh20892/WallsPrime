package com.wallsprime.wallpapers.data.home

import androidx.paging.PagingSource
import androidx.room.*
import com.wallsprime.wallpapers.data.common.UnsplashPhoto

@Dao
interface HomeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUnsplashPhotosHome(photos: List<Home>)

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM home INNER JOIN unsplash_photo ON homeId = id")
    fun getUnsplashPhotoPaged(): PagingSource<Int, UnsplashPhoto>


    @Query("DELETE FROM home")
    suspend fun deleteUnsplashPhotosHome()


}