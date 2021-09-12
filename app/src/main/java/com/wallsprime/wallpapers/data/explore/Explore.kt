package com.wallsprime.wallpapers.data.explore

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "unsplash_explore")
data class UnsplashExplore(
    @PrimaryKey val id: String,
    val name: String,
    val imageUrl: String
    )



@Entity(tableName = "explore_category", primaryKeys = ["PhotoId", "collectionId"])
data class ExploreCategory(
    val PhotoId: String,
    val collectionId: String

)