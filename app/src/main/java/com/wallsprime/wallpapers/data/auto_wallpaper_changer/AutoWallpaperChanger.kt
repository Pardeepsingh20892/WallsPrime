package com.wallsprime.wallpapers.data.auto_wallpaper_changer

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "gallery_photos")
data class GalleryPhotos(
   @PrimaryKey val uri: String

)

