package ru.alox1d.vknewsapp.ui

import android.util.Log
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    Log.d("MainScreen", snackBarHostState.currentSnackbarData.toString())
    val scope = rememberCoroutineScope()
    var fabIsVisible by rememberSaveable {
        mutableStateOf(true)
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        floatingActionButton = {
            if (fabIsVisible) {
                FloatingActionButton(onClick = {
                    scope.launch {
                        val action = snackBarHostState.showSnackbar(
                            message = "This is snackbar",
                            actionLabel = "Hide FAB",
                            duration = SnackbarDuration.Long
                        )
                        if (action == SnackbarResult.ActionPerformed) {
                            fabIsVisible = false
                        }
                    }
                }) {
                    Icon(Icons.Filled.Favorite, contentDescription = null)
                }
            }
        },
        bottomBar = {
            NavigationBar {
                BottomBar()
            }
        }
    ) {

    }
}

@Composable
private fun RowScope.BottomBar() {
    var selectedItemPosition by rememberSaveable {
        mutableStateOf(0)
    }
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Favorite,
        NavigationItem.Profile
    )

    items.forEachIndexed { index, item ->
        NavigationBarItem(
            selected = selectedItemPosition == index,
            onClick = { selectedItemPosition = index },
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