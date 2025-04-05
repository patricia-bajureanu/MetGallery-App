package com.patriciabajureanu.metgallery.network

import com.patriciabajureanu.metgallery.artworks.api.MetArtwork
import com.patriciabajureanu.metgallery.artworks.database.FavouriteDao
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.min
import com.patriciabajureanu.metgallery.artworks.api.SearchArtwork

class MetRepositoryImpl(
    context: Context,
    private val favoriteDao: FavouriteDao
) : MetRepository {

    private val api = AppUser(context).metApi
    private val pageSize = 20

    private suspend fun fetchMetArtworks(objectIds: List<Int>, page: Int): List<MetArtwork?> {
        val startIndex = page * pageSize
        val endIndex = min(startIndex + pageSize, objectIds.size)

        if (startIndex >= endIndex) return emptyList()

        return withContext(Dispatchers.IO) {
            objectIds.subList(startIndex, endIndex).map { objectId ->
                try {
                    val response = api.getArtwork(objectId)
                    response.body()
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

    override suspend fun getMetArtworkDetails(objectId: Int): MetArtwork? {
        val response = api.getArtwork(objectId)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    override suspend fun getArtworks(page: Int): List<MetArtwork?> = withContext(Dispatchers.IO) {
        val response = api.getArtworks()
        val searchArtwork = response.body()

        if (!response.isSuccessful || searchArtwork?.objectIDs.isNullOrEmpty()) {
            return@withContext emptyList()
        }

        fetchMetArtworks(searchArtwork.objectIDs, page)
    }

    override suspend fun getArtworksFromLocal(page: Int): List<MetArtwork?> = withContext(Dispatchers.IO) {
        val favoriteArtworks = favoriteDao.getAll()

        val objectIds = mutableListOf<Int>()
        objectIds.addAll(favoriteArtworks.map { it.id })

        fetchMetArtworks(objectIds, page)
    }

    override suspend fun getArtwork(objectId: Int): MetArtwork? = withContext(Dispatchers.IO) {
        val response = api.getArtwork(objectId)
        return@withContext if (response.isSuccessful) response.body() else null
    }

    override suspend fun searchArtwork(query: String, page: Int): SearchArtwork {
        val response = api.searchArtwork(query)
        return if (response.isSuccessful) {
            response.body() ?: SearchArtwork(total = 0, objectIDs = emptyList())
        } else {
            SearchArtwork(total = 0, objectIDs = emptyList())
        }
    }
}
