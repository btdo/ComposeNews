package com.example.composenews

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppScreen(val drawerIcon: ImageVector, val isRootView: Boolean) {
    Home(Icons.Filled.Home, true),
    Interest(Icons.Filled.ListAlt, true),
    ViewMore(Icons.Filled.ExpandMore, false),
    Article(Icons.Filled.Details, false);

    companion object {
        fun fromRoute(route: String?): AppScreen {
            if (route == null) return values()[0]
            return when (route.substringBefore("/")) {
                Home.name -> Home
                Interest.name -> Interest
                ViewMore.name -> ViewMore
                Article.name -> Article
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
        }
    }
}