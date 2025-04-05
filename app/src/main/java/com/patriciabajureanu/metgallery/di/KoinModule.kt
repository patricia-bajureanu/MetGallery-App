package com.patriciabajureanu.metgallery.di

import com.patriciabajureanu.metgallery.network.MetRepository
import com.patriciabajureanu.metgallery.network.MetRepositoryImpl
import com.patriciabajureanu.metgallery.ui.viewmodel.DetailViewModel
import com.patriciabajureanu.metgallery.ui.viewmodel.SearchViewModel
import com.patriciabajureanu.metgallery.ui.viewmodel.FavouritesViewModel
import com.patriciabajureanu.metgallery.ui.viewmodel.HomeViewModel
import com.patriciabajureanu.metgallery.artworks.database.AppDatabase
import com.patriciabajureanu.metgallery.artworks.database.FavouriteDao
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import android.app.Application
import androidx.room.Room
import androidx.lifecycle.SavedStateHandle

val koinModule = module {
    single { provideDatabase(androidApplication()) }
    single { provideFavoriteData(get()) }
    single<MetRepository> { MetRepositoryImpl(get(), get()) }
    single { androidApplication().applicationContext }

    viewModel { (savedStateHandle: SavedStateHandle) ->
        DetailViewModel(savedStateHandle, get(), get(), get())
    }
    viewModel { SearchViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel {
        FavouritesViewModel(get(), get())
    }

}
val listOfModules = listOf(koinModule)

fun provideDatabase(application: Application): AppDatabase {
    return Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "metropolitan-museum-database"
    ).build()
}

fun provideFavoriteData(database: AppDatabase): FavouriteDao {
    return database.favoriteDao()
}
