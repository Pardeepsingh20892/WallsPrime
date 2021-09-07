package com.wallsprime.wallpapers.data.common

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface RemoteKeyDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(remoteKey: RemoteKey)

    @Query("SELECT * FROM unsplash_photos_remote_keys WHERE collection = :collection")
    suspend fun getRemoteKey(collection: String): RemoteKey

    @Query("DELETE FROM unsplash_photos_remote_keys WHERE collection = :collection")
    suspend fun deleteRemoteKey(collection: String)

}