package com.hadiyarajesh.linkedin_downloader.utility

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.hadiyarajesh.linkedin_downloader.R
import java.io.File

fun downloadFile(
    context: Context,
    fileName: String,
    url: String,
    onDownloadStarted: () -> Unit
) {
    val downloadDirectory =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val appName = context.getString(R.string.app_name).replace(" ", "")
    val appDirectory =
        File(downloadDirectory, appName)

    if (!appDirectory.exists()) {
        appDirectory.mkdir()
    }

    val downloadFilepath = "${appName}/$fileName"

    val uri = Uri.parse(url)

    val downloadRequest = DownloadManager.Request(uri)
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, downloadFilepath)
        .setTitle(fileName)
        .setDescription(context.getString(R.string.download_started))

    downloadRequest.allowScanningByMediaScanner()

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    // Use downloadId in broadcast receiver to receive broadcast for download completion.
    val downloadId = downloadManager.enqueue(downloadRequest)
    onDownloadStarted()
}
