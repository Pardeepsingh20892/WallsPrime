package com.wallsprime.wallpapers.data.home


import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.wallsprime.wallpapers.api.Api
import com.wallsprime.wallpapers.data.common.UnsplashPhoto
import com.wallsprime.wallpapers.data.common.UnsplashPhotoUrls
import com.wallsprime.wallpapers.data.common.RemoteKey
import com.wallsprime.wallpapers.data.common.UnsplashUser
import com.wallsprime.wallpapers.data.database.*
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

private const val PHOTOS_STARTING_PAGE_INDEX = 1


class HomeRemoteMediator(
    private val collection: String,
    private val unsplashApi: Api,
    private val unsplashPhotoDb: UnsplashPhotoDatabase,
    private val refreshOnInit: Boolean
) : RemoteMediator<Int, UnsplashPhoto>()

{
    private val homeDao = unsplashPhotoDb.homeDao()
    private val photoDao = unsplashPhotoDb.photoDao()
    private val favouriteDao = unsplashPhotoDb.favouriteDao()
    private val unsplashRemoteKeyDao = unsplashPhotoDb.remoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UnsplashPhoto>
    ): MediatorResult {

        try {


        val page = when (loadType) {
            LoadType.REFRESH -> PHOTOS_STARTING_PAGE_INDEX
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND ->{ unsplashRemoteKeyDao.getRemoteKey(collection).nextPageKey }

        }


            val response = unsplashApi.getPhotos(page, state.config.pageSize)
            val serverResults = response.body()!!





            val favouritePhotos = favouriteDao.getUnsplashFavouritePhotos().first()
            val collectionImages = serverResults.map { serverResultPhotos ->
                val isFavourite = favouritePhotos.any { favouritePhotos ->
                    favouritePhotos.id == serverResultPhotos.id
                }

                    val url = UnsplashPhotoUrls(
                        serverResultPhotos.urls.raw,
                        serverResultPhotos.urls.full,
                        serverResultPhotos.urls.regular,
                        serverResultPhotos.urls.small

                    )


                    val user = UnsplashUser(
                        serverResultPhotos.user.username
                    )



                    UnsplashPhoto(
                        id = serverResultPhotos.id,
                        urls = url,
                        user = user,
                        favourite = isFavourite

                    )
                }


            val home = serverResults.map { serverResultPhotos ->
                Home(
                    homeId =  serverResultPhotos.id
                )
            }


            val nextPageKey = page + 1

            unsplashPhotoDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    unsplashRemoteKeyDao.deleteRemoteKey(collection)
                    homeDao.deleteUnsplashPhotosHome()


                }


                homeDao.insertUnsplashPhotosHome(home)
                photoDao.insertUnsplashPhotos(collectionImages)
                unsplashRemoteKeyDao.insertRemoteKey(
                    RemoteKey(collection, nextPageKey)
                )

            }








            return MediatorResult.Success(endOfPaginationReached = serverResults?.isEmpty()?: false)


        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }


    }



    override suspend fun initialize(): InitializeAction {
        return if (refreshOnInit) {
            InitializeAction.LAUNCH_INITIAL_REFRESH

        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }


}