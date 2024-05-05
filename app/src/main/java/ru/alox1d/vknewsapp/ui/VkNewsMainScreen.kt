package ru.alox1d.vknewsapp.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.alox1d.vknewsapp.ui.navigation.AppNavGraph
import ru.alox1d.vknewsapp.ui.navigation.NavigationState
import ru.alox1d.vknewsapp.ui.navigation.rememberNavigationState

@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                BottomBar(navigationState)
            }
        }
    ) { paddingValues ->
        // Проблемы:
        // 1. Экраны (composable-функции) не кладутся в backstack - решено с помощью currentBackStackEntryAsState
        // 2. Их состояние не сохраняется: в TextCounter счётчик будет обнулён при переключении между вкладками - решено с помощью rememberSaveable

        AppNavGraph(
            navHostController = navigationState.navHostController,
            newsFeedContent = {
                HomeScreen(
                    paddingValues = paddingValues,
                    onCommentsClick = {
                        navigationState.navigateToComments(it)
                    }
                )
            },
            favoriteContent = {
                TextCounter("Favorite")
            },
            profileContent = {
                TextCounter("Profile")
            },
            commentsScreenContent = { feedPost ->
                CommentsScreen(
                    feedPost = feedPost,
                    onBackPressed = navigationState.navHostController::popBackStack,
                )

                // что делать при клике на системную кнопку "назад"
                BackHandler(
                    onBack = navigationState.navHostController::popBackStack
                )
            }
        )
    }
}

@Composable
private fun RowScope.BottomBar(navigationState: NavigationState) {
    val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Favorite,
        NavigationItem.Profile
    )

    items.forEach { item ->
        val selected = navBackStackEntry?.destination?.hierarchy?.any {
            it.route == item.screen.route
        } ?: false

        NavigationBarItem(
            selected = selected,
            onClick = {
                if (!selected) {
                    navigationState.navigateTo(item.screen.route)
                }
            },
            icon = {
                Icon(imageVector = item.icon, contentDescription = null)
            },
            label = {
                Text(text = stringResource(id = item.titleResId))
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                unselectedTextColor = MaterialTheme.colorScheme.onSecondary,
                indicatorColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
private fun TextCounter(name: String) {
    var count by rememberSaveable {
        mutableIntStateOf(0)
    }
    Text(
        modifier = Modifier.clickable {
            count++
        },
        text = "$name Count: $count", color = Color.Black
    )
}