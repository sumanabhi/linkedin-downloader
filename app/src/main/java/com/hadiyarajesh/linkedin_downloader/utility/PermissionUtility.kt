package com.hadiyarajesh.linkedin_downloader.utility

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.shouldShowRationale
import com.hadiyarajesh.linkedin_downloader.R

@OptIn(ExperimentalPermissionsApi::class)
fun requestPermissionAndPerformAction(
    context: Context,
    permissionState: PermissionState,
    action: () -> Unit
) {
    when (permissionState.status) {
        is PermissionStatus.Granted -> {
            action()
        }

        is PermissionStatus.Denied -> {
            if (!permissionState.status.shouldShowRationale) {
                permissionState.launchPermissionRequest()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.provide_storage_permission_in_settings),
                    Toast.LENGTH_SHORT
                ).show()
                val intent =
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts(
                            "package",
                            context.packageName,
                            null
                        )
                    }
                context.startActivity(intent)
            }
        }
    }
}
