package com.example.composenews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.composenews.ui.Drawer
import com.example.composenews.ui.HomeScreen
import com.example.composenews.ui.InterestScreen
import com.example.composenews.ui.TopBar
import com.example.composenews.ui.theme.ComposeNewsTheme
import kotlinx.coroutines.launch
import java.lang.reflect.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNewsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainApp()
                }
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.fromRoute(backstackEntry.value?.destination?.route)
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                currentScreen = currentScreen,
                rootScreens = AppScreen.values().asList(),
                onDrawerMenuClicked = {
                    coroutineScope.launch { scaffoldState.drawerState.open() }
                },
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        },
        drawerBackgroundColor = MaterialTheme.colors.primarySurface,
        drawerContent = {
            Drawer(currentScreen = currentScreen) {
                navController.navigate(it.name) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
                coroutineScope.launch {
                    scaffoldState.drawerState.close()
                }
            }
        }) {
        Navigation(navController = navController)
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AppScreen.Home.name) {
        composable(AppScreen.Home.name) {
            HomeScreen()
        }
        composable(AppScreen.Interest.name) {
            InterestScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeNewsTheme {
        Surface {
            MainApp()
        }

    }
}