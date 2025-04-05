package com.patriciabajureanu.metgallery.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.patriciabajureanu.metgallery.network.MetRepository
import com.patriciabajureanu.metgallery.artworks.api.MetArtwork

class HomeViewModel(
    private val museumRepo: MetRepository
) : ViewModel() {

    private val _loadingState = MutableStateFlow(false)
    private val _endReached = MutableStateFlow(false)
    private val _museumItems = MutableStateFlow<List<MetArtwork>>(emptyList())

    val loadingState: StateFlow<Boolean> = _loadingState
    val endReached: StateFlow<Boolean> = _endReached
    val museumItems: StateFlow<List<MetArtwork>> = _museumItems

    private var page = 0

    init {
        fetchItems()
    }

    fun fetchItems() {
        if (_loadingState.value || _endReached.value) return

        _loadingState.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val fetchedObjects = museumRepo.getArtworks(page).filterNotNull()
            if (fetchedObjects.isEmpty()) {
                _endReached.value = true
            } else {
                _museumItems.value += fetchedObjects
                page++
            }
            _loadingState.value = false
        }
    }
}
