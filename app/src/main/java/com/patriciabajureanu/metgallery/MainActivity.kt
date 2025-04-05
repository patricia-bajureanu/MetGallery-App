package com.patriciabajureanu.metgallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.patriciabajureanu.metgallery.ui.pages.DetailPage
import com.patriciabajureanu.metgallery.ui.pages.FavouritesPage
import com.patriciabajureanu.metgallery.ui.pages.HomePage
import com.patriciabajureanu.metgallery.ui.pages.SearchPage
import com.patriciabajureanu.metgallery.ui.routes.FavouritesRoute
import com.patriciabajureanu.metgallery.ui.routes.SearchRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomePage(
                onSearch = { navController.navigate(SearchRoute.route) },
                onNavigateToDetail = { objectId, title ->
                    navController.navigate("detail/$objectId/$title")
                },
                onNavigateToFavourites = { navController.navigate(FavouritesRoute.route) }
            )
        }

        composable(SearchRoute.route) {
            SearchPage(
                navController = navController,
                onNavigateToDetail = { objectId, _ ->
                    navController.navigate("detail/$objectId")
                }

            )
        }

        composable("detail/{objectId}/{title}") { backStackEntry ->
            val objectId = backStackEntry.arguments?.getString("objectId")?.toInt() ?: return@composable
            val title = backStackEntry.arguments?.getString("title") ?: "No Title"
            DetailPage(
                onBackPressed = { navController.popBackStack() },
                objectId = objectId,
                title = title
            )
        }



        composable(FavouritesRoute.route) {
            FavouritesPage(
                navigateToHome = { navController.navigate("home") },
                openDetail = { objectId, title ->
                    navController.navigate("detail/$objectId/$title")
                }
            )
        }
    }
}
