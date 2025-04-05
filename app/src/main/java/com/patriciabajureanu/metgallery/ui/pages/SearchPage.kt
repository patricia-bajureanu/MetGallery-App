package com.patriciabajureanu.metgallery.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import com.patriciabajureanu.metgallery.ui.viewmodel.SearchViewModel
import androidx.compose.material.icons.filled.ArrowBack
import com.patriciabajureanu.metgallery.ui.components.LoadingComponent
import com.patriciabajureanu.metgallery.ui.theme.MetGalleryTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card




@Composable
fun SearchPage(
    navController: NavController,
    onNavigateToDetail: (objectId: Int, title: String) -> Unit
) {     val viewModel: SearchViewModel = koinViewModel()

    MetGalleryTheme {
        Scaffold(
            topBar = { SearchTopBar(navController) }
        ) { innerPadding ->
            SearchResults(modifier = Modifier.padding(innerPadding), navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTopBar(navController: NavController, viewModel: SearchViewModel = koinViewModel()) {
    var query by remember { mutableStateOf(viewModel.currentQuery.value.searchTerm) }
    val focusManager = LocalFocusManager.current

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = query,
                onValueChange = { newText ->
                    query = newText
                    viewModel.initiateSearch(query)
                },
                placeholder = { Text("Search for an object") },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = {
                            query = ""
                            viewModel.initiateSearch(query)
                            focusManager.clearFocus()
                        }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear query")
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.initiateSearch(query)
                    focusManager.clearFocus()
                })
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

@Composable
private fun SearchResults(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
    navController: NavController
) {
    val results by viewModel.museumObjects.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isLastPage by viewModel.hasMoreData.collectAsStateWithLifecycle()

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 256.dp),
        contentPadding = PaddingValues(18.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(results) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        navController.navigate("detail/${item.id}/${item.title}")
                    },
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "objectID: ${item.id}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }


        if (isLoading) {
            item {
                LoadingComponent()
            }
        }
        if (!isLoading && !isLastPage) {
            item {
                LaunchedEffect(key1 = results.size) {
                    viewModel.loadMoreItems()
                }
            }
        }

        if (!isLoading && results.isEmpty()) {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "No results found",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}
