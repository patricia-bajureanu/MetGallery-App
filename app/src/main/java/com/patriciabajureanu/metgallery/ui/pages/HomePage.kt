package com.patriciabajureanu.metgallery.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.patriciabajureanu.metgallery.ui.theme.MetGalleryTheme
import com.patriciabajureanu.metgallery.ui.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle



@Composable
fun HomePage(
    onSearch: () -> Unit,
    onNavigateToDetail: (Int, String) -> Unit,
    onNavigateToFavourites: () -> Unit
) {
    MetGalleryTheme {
        Scaffold(
            topBar = { HomeTopBar(onSearch) },
            bottomBar = { HomeBottomBar(onNavigateToFavourites) }
        ) { padding ->
            HomeContent(Modifier.padding(padding), onNavigateToDetail)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(onSearch: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFCBC9C9),
            titleContentColor = Color.White
        ),
        title = {
            Text(
                text = "Search Artwork",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(onClick = onSearch) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search", tint = Color.White)
            }
        }
    )
}

@Composable
fun HomeBottomBar(onNavigateToFavorites: () -> Unit) {
    NavigationBar(containerColor = Color(0xFFF8BBD0)) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorites") },
            label = { Text("Favorites") },
            selected = false,
            onClick = onNavigateToFavorites
        )
    }
}

@Composable
fun HomeContent(modifier: Modifier, onNavigateToDetail: (Int, String) -> Unit) {
    val viewModel: HomeViewModel = koinViewModel()
    val isLoading by viewModel.loadingState.collectAsStateWithLifecycle()

    Column(
        modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)

    ) {
        Text(
            text = "The Metropolitan Museum of Art Gallery",
            style = TextStyle(
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(top = 150.dp)
        )

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Composable
fun MuseumTitle(title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
            .background(Color.White, shape = MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}
