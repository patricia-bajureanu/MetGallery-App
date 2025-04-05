package com.patriciabajureanu.metgallery.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.patriciabajureanu.metgallery.network.MetRepository
import com.patriciabajureanu.metgallery.artworks.api.MetArtwork
import com.patriciabajureanu.metgallery.artworks.database.FavouriteArtwork
import com.patriciabajureanu.metgallery.artworks.database.FavouriteDao

class FavouritesViewModel(
    private val repository: MetRepository,
    private val favoriteDao: FavouriteDao
) : ViewModel() {

    private val _artworksFlow = MutableStateFlow<List<MetArtwork>>(emptyList())
    private val _loadingState = MutableStateFlow(false)
    private val _endReached = MutableStateFlow(false)

    val artworksFlow: StateFlow<List<MetArtwork>> = _artworksFlow
    val loadingState: StateFlow<Boolean> = _loadingState
    val endReached: StateFlow<Boolean> = _endReached

    private var currentPage = 0

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val favouriteArtworks = favoriteDao.getAll()

            _loadingState.value = true
            _endReached.value = false
            _artworksFlow.value = emptyList()
            currentPage = 0
            loadMoreArtworks()

            _loadingState.value = false
        }
    }

    private suspend fun loadMoreArtworks() {
        val artworks = repository.getArtworksFromLocal(currentPage)
        if (artworks.isEmpty()) {
            _endReached.value = true
        } else {
            _artworksFlow.value += artworks.filterNotNull()
            currentPage++
        }
    }

    fun fetchMoreArtworks() {
        if (_loadingState.value || _endReached.value) return

        _loadingState.value = true
        viewModelScope.launch(Dispatchers.IO) {
            loadMoreArtworks()
            _loadingState.value = false
        }
    }
    fun removeFromFavorites(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val artwork = _artworksFlow.value.find { it.id == id } ?: return@launch

            val favouriteArtwork = FavouriteArtwork(
                id = artwork.id,
                title = artwork.title,
                artist = artwork.artist ?: "Unknown",
                image = artwork.image ?: "Unknown"
            )

            favoriteDao.deleteArtwork(favouriteArtwork)
            _artworksFlow.value = _artworksFlow.value.filterNot { it.id == id }
        }
    }



}
