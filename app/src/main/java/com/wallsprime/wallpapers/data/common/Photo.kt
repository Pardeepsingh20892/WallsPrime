package com.wallsprime.wallpapers.data.common


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "unsplash_photo")
data class UnsplashPhoto(
    @PrimaryKey val id: String,
    val favourite: Boolean,
    @Embedded val urls: UnsplashPhotoUrls,
    @Embedded val user: UnsplashUser
)


data class UnsplashPhotoUrls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String
)

data class UnsplashUser(
    val username: String
) {

    val attributionUrl get() = "https://unsplash.com/$username?utm_source=quotemonk&utm_medium=referral"
}



