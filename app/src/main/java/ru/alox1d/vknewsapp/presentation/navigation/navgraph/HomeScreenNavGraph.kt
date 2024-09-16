package ru.alox1d.vknewsapp.presentation.navigation.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import ru.alox1d.vknewsapp.domain.entity.FeedPost
import ru.alox1d.vknewsapp.presentation.navigation.Screen
import ru.alox1d.vknewsapp.presentation.navigation.Screen.Companion.KEY_FEED_POST

fun NavGraphBuilder.homeScreenNavGraph(
    newsFeedContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (FeedPost) -> Unit,
) {
    navigation(
        startDestination = Screen.NewsFeed.route,
        route = Screen.Home.route,
    ) {
        composable(Screen.NewsFeed.route) {
            newsFeedContent()
        }
        composable(
            route = Screen.Comments.route,
            arguments = listOf(
                navArgument(KEY_FEED_POST) {
                    type = FeedPost.NavigationType
                },
            ),
        ) {
            // OLD way by json (String) type:
            // val feedPostJson = it.arguments?.getString(KEY_FEED_POST) ?: ""
            // val feedPost = Gson().fromJson(feedPostJson, FeedPost::class.java)
            val feedPost = it.arguments?.getParcelable<FeedPost>(KEY_FEED_POST) ?: throw RuntimeException("Args are null")

            commentsScreenContent(feedPost)
        }
    }
}