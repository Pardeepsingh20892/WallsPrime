package com.wallsprime.wallpapers.data.common

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "unsplash_photos_remote_keys")
data class RemoteKey(
    @PrimaryKey val collection: String,
    val nextPageKey: Int?

     )
