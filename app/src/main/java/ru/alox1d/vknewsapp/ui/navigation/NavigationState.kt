package ru.alox1d.vknewsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

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

    fun navigateToComments() {
        navHostController.navigate(Screen.Comments.route)
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