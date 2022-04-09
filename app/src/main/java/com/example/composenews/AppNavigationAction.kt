package com.example.composenews

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.composenews.models.ArticleUI
import com.example.composenews.models.ViewMoreCategory
import com.example.composenews.ui.HomeScreen
import com.example.composenews.ui.InterestScreen
import com.example.composenews.ui.ViewMoreScreen


class AppNavigationAction(navController: NavHostController) {
    val navigateToHome: () -> Unit = {
        navController.navigate(AppScreen.Home.name) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
    val navigateToInterests: () -> Unit = {
        navController.navigate(AppScreen.Interest.name) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToViewMore: (viewMore: ViewMoreCategory) -> Unit = {
        navController.navigate("${AppScreen.ViewMore.name}/${it.name}") {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToArticle: (article: ArticleUI) -> Unit = {
        navController.navigate("${AppScreen.Article.name}/${it.title}") {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    navigationActions: AppNavigationAction,
    modifier: Modifier = Modifier
) {

    NavHost(navController = navController, startDestination = AppScreen.Home.name) {
        composable(AppScreen.Home.name) {
            HomeScreen(
                onArticleClicked = navigationActions.navigateToArticle,
                onViewMore = navigationActions.navigateToViewMore,
                modifier = modifier
            )
        }
        composable(AppScreen.Interest.name) {
            InterestScreen()
        }
        composable(
            "${AppScreen.ViewMore.name}/{category}",
            arguments = listOf(navArgument("category") {
                type = NavType.StringType
            })
        ) { entry ->
            val category = entry.arguments?.getString("category") ?: return@composable
            ViewMoreScreen(ViewMoreCategory.valueOf(category))
        }
        composable(
            "${AppScreen.Article.name}/{article}",
            arguments = listOf(navArgument("article") {
                type = NavType.IntType
            })
        ) {

        }
    }
}