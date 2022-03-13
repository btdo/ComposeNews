package com.example.composenews

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppScreen(val drawerIcon: ImageVector){
    Home(Icons.Filled.Home),
    Interest(Icons.Filled.ListAlt);

    companion object {
        fun fromRoute(route: String?): AppScreen {
            if (route == null) return values()[0]
            return when (route.substringBefore("/")) {
                Home.name -> Home
                Interest.name -> Interest
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
        }
    }
}