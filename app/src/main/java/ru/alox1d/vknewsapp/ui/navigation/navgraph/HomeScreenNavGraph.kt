package ru.alox1d.vknewsapp.ui.navigation.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.alox1d.vknewsapp.domain.FeedPost
import ru.alox1d.vknewsapp.ui.navigation.Screen
import ru.alox1d.vknewsapp.ui.navigation.Screen.Companion.KEY_FEED_POST_ID

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
        composable(Screen.Comments.route) { // ROUTE_COMMENTS/{feed_post_id}
            val feedPostId = it.arguments?.getInt(KEY_FEED_POST_ID) ?: 0
            commentsScreenContent(FeedPost(id = feedPostId))
        }
    }
}