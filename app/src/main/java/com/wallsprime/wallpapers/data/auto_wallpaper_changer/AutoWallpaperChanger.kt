package com.wallsprime.wallpapers.data.auto_wallpaper_changer

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "wallpaper_changer")
data class WallpaperChanger(
    @PrimaryKey (autoGenerate = true) val key: Int,
    val isCategoryActive: Boolean,
    val isFavouriteActive: Boolean,
    val isGalleryActive: Boolean,
    val isRandomActive: Boolean

)



@Entity(tableName = "gallery_photos")
data class GalleryPhotos(
   @PrimaryKey val uri: String

)

