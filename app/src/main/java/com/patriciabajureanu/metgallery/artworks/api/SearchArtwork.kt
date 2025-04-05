package com.patriciabajureanu.metgallery.artworks.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchArtwork(
    @Json(name = "total") val total: Int,
    @Json(name = "objectIDs") val objectIDs: List<Int> = emptyList()
)