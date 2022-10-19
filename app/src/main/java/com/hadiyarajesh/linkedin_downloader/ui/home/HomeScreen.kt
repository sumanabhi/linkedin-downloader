package com.hadiyarajesh.linkedin_downloader.ui.home

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.hadiyarajesh.linkedin_downloader.R
import com.hadiyarajesh.linkedin_downloader.model.LinkedInVideo
import com.hadiyarajesh.linkedin_downloader.utility.downloadFile
import com.hadiyarajesh.linkedin_downloader.utility.requestPermissionAndPerformAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val storageWritePermissionState =
        rememberPermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val (url, onUrlChange) = remember { mutableStateOf("") }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = url,
                onValueChange = onUrlChange,
                placeholder = { Text(text = stringResource(id = R.string.enter_linkedin_url_here)) },
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background)
            )
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (
                        !url.startsWith("https://www.linkedin.com") ||
                        url.isBlank()
                    ) {
                        scope.launch {
                            // Show a snack bar when downloading is started
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = context.getString(R.string.provide_valid_linkedin_url),
                            )
                        }
                        return@Button
                    }

                    scope.launch {
                        val linkedInVideo = extractVideoUrl(url)

                        linkedInVideo.url?.let { url ->
                            requestPermissionAndPerformAction(
                                context,
                                storageWritePermissionState
                            ) {
                                downloadFile(
                                    context = context,
                                    fileName = "test.mp4",
                                    url = url,
                                    onDownloadStarted = {
                                        scope.launch {
                                            // Show a snack bar when downloading is started
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message = context.getString(R.string.download_started),
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            ) {
                Text(text = stringResource(id = R.string.download))
            }
        }
    }
}

suspend fun extractVideoUrl(linkedInUrl: String): LinkedInVideo {
    val document = withContext(Dispatchers.IO) {
        Jsoup.connect(linkedInUrl).get()
    }

    val mainContent = document.getElementById("main-content")

    val title = document.title()

    val videoDataSource = mainContent
        ?.firstElementChild()
        ?.firstElementChild()
        ?.select("video")
        ?.first()
        ?.attr("data-sources")

    val videoUrl = videoDataSource
        ?.split(",")
        ?.first()
        ?.removeRange(0, 8)
        ?.removeSurrounding("\"", "\"")

    // In addition to title property, provide other property that we can use as a filename to download it.
    return LinkedInVideo(
        title = title,
        url = videoUrl,
        thumbnail = null
    )
}

val sampleLinkedInUrl =
    "https://www.linkedin.com/posts/hadiyarajesh_this-is-how-you-teach-kids-to-safely-stay-activity-6985974441553866752-Wam-/"