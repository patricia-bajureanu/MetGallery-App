package com.patriciabajureanu.metgallery.artworks.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM favouriteArtwork")
    fun getAll(): List<FavouriteArtwork>

    @Query("SELECT EXISTS(SELECT 1 FROM favouriteArtwork WHERE id = :id)")
    fun exists(id: Int): Flow<Boolean>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArtwork(vararg artworks: FavouriteArtwork)
    @Delete
    fun deleteArtwork(favoriteArtwork: FavouriteArtwork)
}