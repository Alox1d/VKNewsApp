package ru.alox1d.vknewsapp.presentation.navigation

import android.net.Uri
import com.google.gson.Gson
import ru.alox1d.vknewsapp.domain.entity.FeedPost

sealed class Screen(
    val route: String
) {

    data object NewsFeed : Screen(ROUTE_NEWS_FEED)
    data object Favorite : Screen(ROUTE_FAVORITE)
    data object Profile : Screen(ROUTE_PROFILE)
    data object Home : Screen(ROUTE_HOME)
    data object Comments : Screen(ROUTE_COMMENTS) {

        private const val ROUTE_FOR_ARGS = "ROUTE_COMMENTS"

        // OLD: ROUTE_COMMENTS/{feed_post_id}
        fun getRouteWithArgs(feedPost: FeedPost): String {
            val feedPostJson = Gson().toJson(feedPost)
            return "$ROUTE_FOR_ARGS/${feedPostJson.encode()}"
        }
    }

    companion object {

        const val KEY_FEED_POST = "feed_post"

        const val ROUTE_HOME = "ROUTE_HOME"
        const val ROUTE_COMMENTS = "ROUTE_COMMENTS/{$KEY_FEED_POST}"
        const val ROUTE_NEWS_FEED = "ROUTE_NEWS_FEED"
        const val ROUTE_FAVORITE = "ROUTE_FAVORITE"
        const val ROUTE_PROFILE = "ROUTE_PROFILE"
    }
}

fun String.encode(): String {
    return Uri.encode(this)
}