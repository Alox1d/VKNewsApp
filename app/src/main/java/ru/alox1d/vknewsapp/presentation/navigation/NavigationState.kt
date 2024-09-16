package ru.alox1d.vknewsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.alox1d.vknewsapp.domain.entity.FeedPost

class NavigationState(
    val navHostController: NavHostController,
) {

    fun navigateTo(route: String) {
        navHostController.navigate(route) {
            launchSingleTop = true

            // findStartDestination необходим для вложенной навигации.
            // без него будет краш при попытке "выкинуть" все экраны с помощью popUpTo, т.к.
            popUpTo(navHostController.graph.findStartDestination().id) {

                saveState = true
                // НО, несмотя на saveState = true,  при нажатии "назад" стейт текущего экрана сохранён всё равно не будет.
                // Это нормальное поведение, ведь мы с него осознанно ушли
            }
            restoreState = true
        }
    }

    fun navigateToComments(feedPost: FeedPost) {
        navHostController.navigate(Screen.Comments.getRouteWithArgs(feedPost)) // ROUTE_COMMENTS/{feed_post_id}
        // other possible examples: ROUTE_COMMENTS/15/name_of_community, ROUTE_COMMENTS/{feed_post_id}/{name}/...
    }
}

@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController()
): NavigationState {
    return remember {
        NavigationState(navHostController)
    }
}