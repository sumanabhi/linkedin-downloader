package com.hadiyarajesh.linkedin_downloader.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.hadiyarajesh.linkedin_downloader.ui.navigation.LinkedInDownloaderNavigation
import com.hadiyarajesh.linkedin_downloader.ui.theme.LinkedInDownloaderTheme

@Composable
fun LinkedInDownloaderApp() {
    LinkedInDownloaderTheme {
        val navController = rememberNavController()

        Scaffold { innerPadding ->
            LinkedInDownloaderNavigation(
                modifier = Modifier.padding(innerPadding),
                navController = navController
            )
        }
    }
}
