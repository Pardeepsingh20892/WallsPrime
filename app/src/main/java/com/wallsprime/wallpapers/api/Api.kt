package com.wallsprime.wallpapers.api


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
       const val CLIENT_ID ="sccg6dRb-_MccdnUrjBHYFYWPXrj6KX399NCzTgt-PU"
    }

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): ApiResponse

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("users/wendyyoung123/likes")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<DataModel>>


    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
     @GET("collections/{id}/photos")
    suspend fun getCollectionPhotos(
         @Path("id")  id: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<DataModel>>


    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("photos/{id}")
    suspend fun getRandomPhotos(
        @Path("id")  id: String
    ): Response<DataModel>


    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("photos/{id}/download")
    suspend fun download(
        @Path("id")  id: String
    )


}

