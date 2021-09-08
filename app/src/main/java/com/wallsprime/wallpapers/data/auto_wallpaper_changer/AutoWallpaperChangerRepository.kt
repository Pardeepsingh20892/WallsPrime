package com.wallsprime.wallpapers.data.auto_wallpaper_changer

import com.wallsprime.wallpapers.data.database.UnsplashPhotoDatabase
import com.wallsprime.wallpapers.data.explore.UnsplashExplore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AutoWallpaperChangerRepository@Inject constructor(
    private val unsplashPhotoDb: UnsplashPhotoDatabase
) {

    private val autoWallpaperChangerDao = unsplashPhotoDb.autoWallpaperChangerDao()

}