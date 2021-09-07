package com.wallsprime.wallpapers.di


import android.app.Application
import com.wallsprime.wallpapers.api.Api
import com.wallsprime.wallpapers.data.database.UnsplashPhotoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideUnsplashApi(retrofit: Retrofit): Api =
        retrofit.create(Api::class.java)



    @Provides
    @Singleton
    fun provideDatabase(app: Application): UnsplashPhotoDatabase = UnsplashPhotoDatabase.getInstance(app)





}