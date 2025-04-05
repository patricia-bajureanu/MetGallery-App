package com.patriciabajureanu.metgallery.ui.pages

import kotlinx.serialization.Serializable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.patriciabajureanu.metgallery.ui.components.NavigationBar
import com.patriciabajureanu.metgallery.ui.components.ArtworkCard
import com.patriciabajureanu.metgallery.ui.viewmodel.FavouritesViewModel
import com.patriciabajureanu.metgallery.ui.theme.MetGalleryTheme
import org.koin.androidx.compose.koinViewModel
import com.patriciabajureanu.metgallery.ui.components.LoadingIndicator

@Serializable
object FavouritesPage

@Composable
fun FavouritesPage(
    navigateToHome: () -> Unit,
    openDetail: (id: Int, title: String) -> Unit
) {
    MetGalleryTheme {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    currentScreen = "Favorites",
                    goToHome = navigateToHome,
                    goToFavorites = {}
                )
            }
        ) { paddingValues ->
            FavouritesContent(
                modifier = Modifier.padding(paddingValues),
                openDetail = openDetail
            )
        }
    }
}

@Composable
private fun FavouritesContent(
    modifier: Modifier = Modifier,
    viewModel: FavouritesViewModel = koinViewModel(),
    openDetail: (id: Int, title: String) -> Unit
) {
    val favorites by viewModel.artworksFlow.collectAsStateWithLifecycle()
    val loading by viewModel.loadingState.collectAsStateWithLifecycle()
    val reachedEnd by viewModel.endReached.collectAsStateWithLifecycle()

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(256.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(favorites) { _, item ->
            ArtworkCard(
                item = item,
                onClick = { openDetail(item.id, item.title) },
                onRemove = { viewModel.removeFromFavorites(item.id) }
            )
        }

        if (loading) {
            item { LoadingIndicator() }
        }

        if (!loading && !reachedEnd) {
            item {
                LaunchedEffect(favorites.size) {
                    viewModel.fetchMoreArtworks()
                }
            }
        }
    }
}

