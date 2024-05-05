package ru.alox1d.vknewsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.homeScreenNavGraph(
    newsFeedContent: @Composable () -> Unit,
    commentsScreenContent: @Composable () -> Unit,
) {
    navigation(
        startDestination = Screen.NewsFeed.route,
        route = Screen.Home.route,
    ) {
        composable(Screen.NewsFeed.route) {
            newsFeedContent()
        }
        composable(Screen.Comments.route) {
            commentsScreenContent()
        }
    }
}