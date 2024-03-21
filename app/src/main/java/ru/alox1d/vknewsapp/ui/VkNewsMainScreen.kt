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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.alox1d.vknewsapp.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val selectedNavItem by viewModel.selectedNavItem.observeAsState(NavigationItem.Home)

    Scaffold(
        bottomBar = {
            NavigationBar {
                BottomBar(viewModel, selectedNavItem)
            }
        }
    ) { paddingValues ->
        when (selectedNavItem) {
            // Проблемы:
            // 1. Функции не кладутся в backstack
            // 2. Их состояние не сохраняется: в TextCounter счётчик будет обнулён при переключении между вкладками
            NavigationItem.Home -> HomeScreen(viewModel, paddingValues)
            NavigationItem.Favorite -> TextCounter("Favorite")
            NavigationItem.Profile -> TextCounter("Profile")
        }
    }
}

@Composable
private fun RowScope.BottomBar(viewModel: MainViewModel, selectedNavItem: NavigationItem) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Favorite,
        NavigationItem.Profile
    )

    items.forEach { item ->
        NavigationBarItem(
            selected = selectedNavItem == item,
            onClick = { viewModel.selectNavItem(item) },
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
    var count by remember {
        mutableIntStateOf(0)
    }
    Text(
        modifier = Modifier.clickable {
            count++
        },
        text = "$name Count: $count", color = Color.Black
    )
}