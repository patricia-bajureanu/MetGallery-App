package com.patriciabajureanu.metgallery.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.patriciabajureanu.metgallery.ui.theme.MetGalleryTheme
import com.patriciabajureanu.metgallery.ui.viewmodel.DetailViewModel
import org.koin.androidx.compose.koinViewModel
import com.patriciabajureanu.metgallery.ui.components.LoadingComponent
import androidx.lifecycle.SavedStateHandle
import org.koin.core.parameter.parametersOf
import android.R.attr.title

@Composable
fun DetailPage(onBackPressed: () -> Unit, objectId: Int, title: String) {
    val savedStateHandle = SavedStateHandle().apply {
        this["objectId"] = objectId
        this["title"] = title
    }

    val viewModel: DetailViewModel = koinViewModel(parameters = { parametersOf(savedStateHandle) })

    val isLoading by viewModel.searchingFlow.collectAsStateWithLifecycle()

    MetGalleryTheme {
        Scaffold(
            topBar = {
                DetailPageTopBar(onBackPressed, title)
            },
        ) { innerPadding ->
            if (isLoading) {
                LoadingComponent()
            } else {
                MetDetails(modifier = Modifier.padding(innerPadding), viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailPageTopBar(onBackPressed: () -> Unit, title: String, viewModel: DetailViewModel = koinViewModel()) {
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFCBC9C9),
            titleContentColor = Color.White
        ),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = {
                viewModel.toggleFavorite()
            }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Color.White
                )
            }
        }
    )
}


@Composable
private fun MetInfo(label: String?, value: String?) {
    if (label != null && value != null && label.isNotBlank() && value.isNotBlank()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFFB0BEC5)),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = value,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF263238)),
                fontWeight = FontWeight.Normal
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
private fun MetDetails(modifier: Modifier = Modifier, viewModel: DetailViewModel = koinViewModel()) {
    val museumData by viewModel.museumFlow.collectAsStateWithLifecycle()
    val museumItem = museumData ?: return

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
            .verticalScroll(scrollState)
            .background(Color(0xFFFFFFFF)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = museumItem.title,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF37474F)
        )
        Text(
            text = "Object ID: ${museumItem.id}",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF37474F)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (museumItem.image.isNotBlank()) {
            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.6f)
                    .padding(6.dp),
                model = museumItem.image,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        MetInfo("Artist:", museumItem.artist)
        MetInfo("Date:", museumItem.date)
        MetInfo("Department:", museumItem.department)
        MetInfo("Country:", museumItem.country)
    }
}