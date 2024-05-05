package ru.alox1d.vknewsapp.ui.navigation

sealed class Screen(
    val route: String
) {

    data object NewsFeed : Screen(ROUTE_NEWS_FEED)
    data object Favorite : Screen(ROUTE_FAVORITE)
    data object Profile : Screen(ROUTE_PROFILE)
    data object Home : Screen(ROUTE_HOME)
    data object Comments : Screen(ROUTE_COMMENTS)

    private companion object {

        const val ROUTE_HOME = "ROUTE_HOME"
        const val ROUTE_COMMENTS = "ROUTE_COMMENTS"
        const val ROUTE_NEWS_FEED = "ROUTE_NEWS_FEED"
        const val ROUTE_FAVORITE = "ROUTE_FAVORITE"
        const val ROUTE_PROFILE = "ROUTE_PROFILE"
    }
}