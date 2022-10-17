package com.hadiyarajesh.linkedin_downloader.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hadiyarajesh.linkedin_downloader.ui.home.HomeScreen
import com.hadiyarajesh.linkedin_downloader.ui.home.HomeViewModel

@Composable
fun LinkedInDownloaderNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        composable(route = Screens.Home.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel
            )
        }
    }
}
