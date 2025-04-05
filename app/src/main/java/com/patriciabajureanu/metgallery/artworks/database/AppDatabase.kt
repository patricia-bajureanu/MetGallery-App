package com.patriciabajureanu.metgallery.artworks.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavouriteArtwork::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavouriteDao
}