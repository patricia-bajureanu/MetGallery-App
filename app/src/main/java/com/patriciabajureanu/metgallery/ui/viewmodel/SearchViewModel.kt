package com.patriciabajureanu.metgallery.ui.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import com.patriciabajureanu.metgallery.network.MetRepository
import com.patriciabajureanu.metgallery.artworks.api.MetArtwork

data class SearchQueryState(
    val searchTerm: String,
    val pageNumber: Int
)

@SuppressLint("StaticFieldLeak")
class SearchViewModel(
    private val repository: MetRepository
) : ViewModel() {

    private val _currentQuery = MutableStateFlow(SearchQueryState("", 0))
    private val _loadingState = MutableStateFlow(false)
    private val _museumObjects = MutableStateFlow<List<MetArtwork>>(emptyList())
    private val _hasMoreData = MutableStateFlow(true)

    val currentQuery: StateFlow<SearchQueryState> = _currentQuery
    val isLoading: StateFlow<Boolean> = _loadingState
    val museumObjects: StateFlow<List<MetArtwork>> = _museumObjects
    val hasMoreData: StateFlow<Boolean> = _hasMoreData

    init {
        observeSearchQuery()
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            _currentQuery
                .debounce(300)
                .filterNot { query -> query.searchTerm.isBlank() }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    performSearch(query.searchTerm, query.pageNumber)
                }
                .collect { results ->
                    updateSearchResults(results)
                }
        }
    }

    private fun performSearch(query: String, page: Int) = flow {
        _loadingState.value = true
        try {
            val searchResults = repository.searchArtwork(query, page)

            if (searchResults.objectIDs.isEmpty()) {
                _hasMoreData.value = false
            }

            searchResults.objectIDs.forEach { objectID ->
                val metArtwork = repository.getArtwork(objectID)
                metArtwork?.let {
                    emit(it)
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        } finally {
            _loadingState.value = false
        }
    }


    private fun updateSearchResults(results: MetArtwork) {
        _museumObjects.value = _museumObjects.value + results
    }

    fun loadMoreItems() {
        if (_loadingState.value || _currentQuery.value.searchTerm.isBlank() || !_hasMoreData.value) return
        _currentQuery.value = _currentQuery.value.copy(pageNumber = _currentQuery.value.pageNumber + 1)
    }

    fun initiateSearch(query: String) {
        _hasMoreData.value = true
        _museumObjects.value = emptyList()
        _currentQuery.value = SearchQueryState(searchTerm = query, pageNumber = 0)
    }
}
