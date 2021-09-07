package com.wallsprime.wallpapers.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.wallsprime.wallpapers.data.auto_wallpaper_changer.AutoWallpaperChangerDao
import com.wallsprime.wallpapers.data.common.PhotoDao
import com.wallsprime.wallpapers.data.common.RemoteKey
import com.wallsprime.wallpapers.data.common.RemoteKeyDao
import com.wallsprime.wallpapers.data.explore.ExploreCategory
import com.wallsprime.wallpapers.data.explore.UnsplashExplore
import com.wallsprime.wallpapers.data.explore.ExploreDao
import com.wallsprime.wallpapers.data.home.Home
import com.wallsprime.wallpapers.data.common.UnsplashPhoto
import com.wallsprime.wallpapers.data.favourite.FavouriteDao
import com.wallsprime.wallpapers.data.home.HomeDao
import com.wallsprime.wallpapers.worker.SeedDatabaseWorker


@Database(
  entities = [UnsplashPhoto::class, RemoteKey::class, UnsplashExplore::class, ExploreCategory::class, Home::class ],
  version = 14,exportSchema = false
)
  abstract class UnsplashPhotoDatabase : RoomDatabase() {


  abstract fun photoDao(): PhotoDao
  abstract fun homeDao(): HomeDao
  abstract fun exploreDao(): ExploreDao
  abstract fun favouriteDao(): FavouriteDao
  abstract fun remoteKeyDao(): RemoteKeyDao
  abstract fun autoWallpaperChangerDao(): AutoWallpaperChangerDao




  companion object {

    // For Singleton instantiation
    @Volatile private var instance: UnsplashPhotoDatabase? = null

    fun getInstance(context: Context): UnsplashPhotoDatabase {
      return instance ?: synchronized(this) {
        instance ?: buildDatabase(context).also { instance = it }
      }
    }

    private fun buildDatabase(context: Context): UnsplashPhotoDatabase {
      return Room.databaseBuilder(context, UnsplashPhotoDatabase::class.java, "unsplash_photo_database")
        .addCallback(
          object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
              super.onCreate(db)
              val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
              WorkManager.getInstance(context).enqueue(request)
            }
          }
        )
        .fallbackToDestructiveMigration()
        .build()
    }
  }


}









