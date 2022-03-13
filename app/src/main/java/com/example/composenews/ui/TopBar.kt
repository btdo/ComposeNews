package com.example.composenews.ui

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.composenews.AppScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopBar(
    currentScreen: AppScreen,
    rootScreens: List<AppScreen>,
    onDrawerMenuClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    TopAppBar(
        title = { Text(text = currentScreen.name, style = MaterialTheme.typography.h6) },
        navigationIcon = {
            if (rootScreens.contains(currentScreen)) {
                IconButton(onClick = onDrawerMenuClicked) {
                    Icon(Icons.Filled.Menu, "")
                }
            } else {
                IconButton(onClick = onBackClicked) {
                    Icon(Icons.Filled.ArrowBack, "Back")
                }
            }
        }
    )
}

@Preview(showBackground = false)
@Composable
fun TopBarPreview() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val navController = rememberNavController()
    TopBar(
        AppScreen.Home,
        AppScreen.values().asList(),
        onDrawerMenuClicked = {},
        onBackClicked = {}
    )
}

@Preview(showBackground = false)
@Composable
fun TopBarPreviewBack() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val navController = rememberNavController()
    TopBar(
        AppScreen.Interest,
        listOf(AppScreen.Home),
        onDrawerMenuClicked = {},
        onBackClicked = {}
    )
}