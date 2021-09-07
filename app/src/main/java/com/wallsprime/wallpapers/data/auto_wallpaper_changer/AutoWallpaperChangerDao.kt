package com.wallsprime.wallpapers.data.auto_wallpaper_changer

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.wallsprime.wallpapers.data.explore.UnsplashExplore
import kotlinx.coroutines.flow.Flow


@Dao
interface AutoWallpaperChangerDao {


    // auto wallpaper changer
    @Query("SELECT * FROM unsplash_explore")
    fun  getUnsplashWallpaperCategory(): Flow<List<UnsplashExplore>>

    @Query("SELECT * FROM unsplash_explore WHERE AutoWallpaperChanger = 1")
    suspend fun  getUnsplashCategoryChanger(): List<UnsplashExplore>

    @Update
    suspend fun updateUnsplashCategory(photos: UnsplashExplore)




}