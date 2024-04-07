package ru.alox1d.vknewsapp.ui

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.alox1d.vknewsapp.MainViewModel
import ru.alox1d.vknewsapp.ui.navigation.AppNavGraph
import ru.alox1d.vknewsapp.ui.navigation.Screen

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val navHostController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                BottomBar(navHostController)
            }
        }
    ) { paddingValues ->
        // Проблемы:
        // 1. Экраны (composable-функции) не кладутся в backstack - решено с помощью currentBackStackEntryAsState
        // 2. Их состояние не сохраняется: в TextCounter счётчик будет обнулён при переключении между вкладками - решено с помощью rememberSaveable

        AppNavGraph(
            navHostController = navHostController,
            homeScreenContent = {
                HomeScreen(viewModel, paddingValues)
            },
            favoriteContent = {
                TextCounter("Favorite")
            },
            profileContent = {
                TextCounter("Profile")
            }
        )
    }
}

@Composable
private fun RowScope.BottomBar(navHostController: NavHostController) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Favorite,
        NavigationItem.Profile
    )

    items.forEach { item ->
        NavigationBarItem(
            selected = currentRoute == item.screen.route,
            onClick = {
                navHostController.navigate(item.screen.route) {
                    launchSingleTop = true
                    popUpTo(Screen.NewsFeed.route) {
                        // НО при нажатии "назад" стейт текущего экрана сохранён всё равно не будет.
                        // Это нормальное поведение, ведь мы с него осознанно ушли
                        saveState = true
                    }
                    restoreState = true
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