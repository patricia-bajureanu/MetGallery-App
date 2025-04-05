package com.patriciabajureanu.metgallery.network

import com.patriciabajureanu.metgallery.artworks.api.MetArtwork
import com.patriciabajureanu.metgallery.artworks.api.SearchArtwork


interface MetRepository {
    suspend fun getArtworks(page: Int): List<MetArtwork?>

    suspend fun getArtworksFromLocal(page: Int): List<MetArtwork?>

    suspend fun getArtwork(objectId: Int): MetArtwork?

    suspend fun searchArtwork(query: String, page: Int): SearchArtwork

    suspend fun getMetArtworkDetails(objectId: Int): MetArtwork?
}
