package com.patriciabajureanu.metgallery.artworks.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MetArtwork(
    @Json(name = "objectID") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "artistDisplayName") val artist: String,
    @Json(name = "primaryImageSmall") val image: String,
    @Json(name = "objectDate") val date: String,
    @Json(name = "department") val department: String,
    @Json(name = "country") val country: String
)