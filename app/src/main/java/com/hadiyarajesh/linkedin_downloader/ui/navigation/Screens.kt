package com.hadiyarajesh.linkedin_downloader.ui.navigation

import androidx.annotation.DrawableRes
import com.hadiyarajesh.linkedin_downloader.R

sealed class Screens(
    val route: String,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int
) {
    object Home : Screens(
        route = "Home",
        icon = R.drawable.ic_launcher_foreground,
        selectedIcon = R.drawable.ic_launcher_foreground
    )

    fun withArgs(vararg args: Any): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
