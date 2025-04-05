package com.patriciabajureanu.metgallery.ui.viewmodel

import com.patriciabajureanu.metgallery.network.MetRepository
import com.patriciabajureanu.metgallery.artworks.api.MetArtwork
import com.patriciabajureanu.metgallery.artworks.database.FavouriteDao
import com.patriciabajureanu.metgallery.artworks.database.FavouriteArtwork
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import kotlinx.coroutines.withContext

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val metRepository: MetRepository,
    private val context: Context,
    private val favouriteDao: FavouriteDao
) : ViewModel() {

    private val objectId: Int = savedStateHandle["objectId"] ?: 0
    private val title: String = savedStateHandle["title"] ?: ""

    private val _isFavorite = MutableStateFlow(false)
    private val _museumFlow = MutableStateFlow<MetArtwork?>(null)
    private val _searchingFlow = MutableStateFlow(true)

    val isFavorite: StateFlow<Boolean> = _isFavorite
    val museumFlow: StateFlow<MetArtwork?> = _museumFlow
    val searchingFlow: StateFlow<Boolean> = _searchingFlow

    init {
        loadMuseumObject()
        checkIfFavorite()
    }

    private fun loadMuseumObject() {
        viewModelScope.launch(Dispatchers.IO) {
            _searchingFlow.value = true
            try {
                val metArtwork = metRepository.getArtwork(objectId)
                _museumFlow.value = metArtwork
            } catch (exception: IOException) {
                exception.printStackTrace()
            } finally {
                _searchingFlow.value = false
            }
        }
    }

    private fun checkIfFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            favouriteDao.exists(objectId).collectLatest { exists ->
                _isFavorite.value = exists
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            val favouriteArtwork = FavouriteArtwork(
                id = objectId,
                title = _museumFlow.value?.title ?: "Unknown",
                artist = _museumFlow.value?.artist ?: "Unknown",
                image = _museumFlow.value?.image ?: "Unknown"
            )
            if (_isFavorite.value) {
                favouriteDao.deleteArtwork(favouriteArtwork)
            } else {
                favouriteDao.insertArtwork(favouriteArtwork)
            }
            showFavoriteToast()
        }
    }

    private suspend fun showFavoriteToast() {
        val message = if (_isFavorite.value) "Removed from Favorites" else "Added to Favorites"
        withContext(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
