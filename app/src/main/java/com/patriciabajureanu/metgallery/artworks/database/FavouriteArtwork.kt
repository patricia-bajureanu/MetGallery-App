package com.patriciabajureanu.metgallery.artworks.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouriteArtwork(
    @PrimaryKey val id: Int,
    val title: String,
    val artist: String,
    val image: String
)
