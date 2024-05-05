package ru.alox1d.vknewsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    newsFeedContent: @Composable () -> Unit,
    favoriteContent: @Composable () -> Unit,
    profileContent: @Composable () -> Unit,
    commentsScreenContent: @Composable () -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route
    ) {
        homeScreenNavGraph(
            newsFeedContent = newsFeedContent,
            commentsScreenContent = commentsScreenContent
        )
        composable(Screen.Favorite.route) {
            favoriteContent()
        }
        composable(Screen.Profile.route) {
            profileContent()
        }
    }
}