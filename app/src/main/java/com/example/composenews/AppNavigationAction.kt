package com.example.composenews

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.composenews.models.ArticleUI
import com.example.composenews.models.ViewMoreCategory
import com.example.composenews.ui.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

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
        navController.navigate("${AppScreen.Article.name}/${it.id}") {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun NavigationGraph(
    navController: NavHostController,
    navigationActions: AppNavigationAction,
    modifier: Modifier = Modifier
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = AppScreen.Home.name,
        enterTransition = {
            slideInVertically(initialOffsetY = { 1000 })
        },
        exitTransition = {
            fadeOut(animationSpec = tween(0))
        },
        popEnterTransition = { slideInVertically(initialOffsetY = { -1000 }) },
        popExitTransition = { fadeOut(animationSpec = tween(0)) }) {
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
                type = NavType.StringType
            }
            )
        ) { entry ->
            val id = entry.arguments?.getString("article") ?: return@composable
            ArticleScreen(articleId = id)
        }
    }
}