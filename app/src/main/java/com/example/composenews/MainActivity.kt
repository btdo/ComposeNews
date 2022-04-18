package com.example.composenews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.composenews.ui.Drawer
import com.example.composenews.ui.TopBar
import com.example.composenews.ui.theme.ComposeNewsTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeNewsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainApp(viewModel)
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun MainApp(viewModel: MainViewModel) {
    AppScaffolded()
}

@ExperimentalAnimationApi
@Composable
fun AppScaffolded(
    navController: NavHostController = rememberAnimatedNavController()
) {
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.fromRoute(backstackEntry.value?.destination?.route)
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val coroutineScope = rememberCoroutineScope()
    val navigationActions = remember(navController) {
        AppNavigationAction(navController)
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                currentScreen = currentScreen,
                rootScreens = AppScreen.values().filter { it.isRootView },
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
                when (it) {
                    AppScreen.Home -> navigationActions.navigateToHome()
                    AppScreen.Interest -> navigationActions.navigateToInterests()
                }
                coroutineScope.launch {
                    scaffoldState.drawerState.close()
                }
            }
        }) {
        val padding = Modifier.padding(it)
        NavigationGraph(
            navController = navController,
            navigationActions = navigationActions,
            padding
        )
    }
}


@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeNewsTheme {
        Surface {
            AppScaffolded()
        }
    }
}