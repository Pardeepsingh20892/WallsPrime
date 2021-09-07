package com.wallsprime.wallpapers.data.common

import androidx.room.*


@Dao
interface PhotoDao{



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUnsplashPhotos(photos: List<UnsplashPhoto>)

    @Update
    suspend fun updateUnsplashPhotos(photos: UnsplashPhoto)

    @Query("DELETE FROM unsplash_photo WHERE favourite = 0")
    suspend fun deleteUnsplashPhotos()






}