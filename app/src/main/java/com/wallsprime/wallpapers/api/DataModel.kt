package com.wallsprime.wallpapers.api



    data class DataModel(
        val id: String,
        val urls: UnsplashPhotoUrls,
        val user: UnsplashUser
)


    data class UnsplashPhotoUrls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String
    )

    data class UnsplashUser(
        val username: String
    )
