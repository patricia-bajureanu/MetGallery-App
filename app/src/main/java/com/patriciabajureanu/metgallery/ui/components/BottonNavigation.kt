package com.patriciabajureanu.metgallery.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem

@Composable
fun NavigationBar(
    currentScreen: String,
    goToHome: () -> Unit,
    goToFavorites: () -> Unit
) {
    BottomNavigation(
        backgroundColor = Color(0xFFF8BBD0),
        contentColor = Color.White
    ) {
        BottomNavigationItem(
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentScreen == "Home",
            onClick = goToHome
        )

        BottomNavigationItem(
            icon = { Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favorites") },
            label = { Text("Favorites") },
            selected = currentScreen == "Favorites",
            onClick = goToFavorites
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NavigationBarPreview() {
    NavigationBar(
        currentScreen = "Home",
        goToHome = {},
        goToFavorites = {}
    )
}
