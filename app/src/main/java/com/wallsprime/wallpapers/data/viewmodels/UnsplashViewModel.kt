package com.wallsprime.wallpapers.data.viewmodels

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wallsprime.wallpapers.data.common.PhotoRepository
import com.wallsprime.wallpapers.data.explore.ExploreRepository
import com.wallsprime.wallpapers.data.common.UnsplashPhoto
import com.wallsprime.wallpapers.data.favourite.FavouriteRepository
import com.wallsprime.wallpapers.data.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UnsplashViewModel@Inject constructor(
    private val photoRepository: PhotoRepository,
    private val homeRepository: HomeRepository,
    private val exploreRepository: ExploreRepository,
    private val favouriteRepository: FavouriteRepository
) : ViewModel() {


    var currentCollectionId: String? = null
    var exploreCurrentPosition: Int? = null
    var homeCurrentPosition: Int? = null
    var favouriteCurrentPosition: Int? = null
    var refreshOnInit = true
    var exploreResults: LiveData<PagingData<UnsplashPhoto>>? = null


    var homeResults = homeRepository.getPhotoResultsPaged("HomeFragmentKey",refreshOnInit).cachedIn(viewModelScope).asLiveData()
    var favouriteResults = favouriteRepository.getUnsplashFavouritePhotos().stateIn(viewModelScope, SharingStarted.Lazily, null)


    fun setExploreResult(){
        if (currentCollectionId != null){
            exploreResults = exploreRepository.getExploreResultsPaged(currentCollectionId!!,refreshOnInit).cachedIn(viewModelScope).asLiveData()
        }
    }



    suspend fun getPhotoCategory() = exploreRepository.getCategory()



    fun onFavouriteClick(photo: UnsplashPhoto) {
        val currentlyFavourite = photo.favourite
        val updatedPhoto = photo.copy(favourite = !currentlyFavourite)
        viewModelScope.launch {
            photoRepository.updateUnsplashPhoto(updatedPhoto)
           // photoRepository.download(photo.id)
        }
    }




    suspend fun download(id: String) = photoRepository.download(id)



    private val _downloading: MutableLiveData<Boolean> = MutableLiveData()
    val downloading: LiveData<Boolean> = _downloading

    fun setDownloading(downloading: Boolean) {
        _downloading.value = downloading
    }



}



