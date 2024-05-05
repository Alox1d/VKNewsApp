package ru.alox1d.vknewsapp.ui.navigation

import ru.alox1d.vknewsapp.domain.FeedPost

sealed class Screen(
    val route: String
) {

    data object NewsFeed : Screen(ROUTE_NEWS_FEED)
    data object Favorite : Screen(ROUTE_FAVORITE)
    data object Profile : Screen(ROUTE_PROFILE)
    data object Home : Screen(ROUTE_HOME)
    data object Comments : Screen(ROUTE_COMMENTS) {

        private const val ROUTE_FOR_ARGS = "ROUTE_COMMENTS"

        // ROUTE_COMMENTS/{feed_post_id}
        fun getRouteWithArgs(feedPost: FeedPost): String {
            return "$ROUTE_FOR_ARGS/${feedPost.id}/${feedPost.contextText}"
        }
    }

    companion object {

        const val KEY_FEED_POST_ID = "feed_post_id"
        const val KEY_CONTEXT_TEXT_ID = "context_text"

        const val ROUTE_HOME = "ROUTE_HOME"
        const val ROUTE_COMMENTS = "ROUTE_COMMENTS/{$KEY_FEED_POST_ID}/{$KEY_CONTEXT_TEXT_ID}"
        const val ROUTE_NEWS_FEED = "ROUTE_NEWS_FEED"
        const val ROUTE_FAVORITE = "ROUTE_FAVORITE"
        const val ROUTE_PROFILE = "ROUTE_PROFILE"
    }
}