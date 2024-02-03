package ru.alox1d.vknewsapp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import ru.alox1d.vknewsapp.R

sealed class NavigationItem(
    val titleResId: Int,
    val icon: ImageVector
) {

    object Home : NavigationItem(R.string.navigation_item_home, Icons.Outlined.Home)
    object Favorite : NavigationItem(R.string.navigation_item_favorite, Icons.Outlined.Favorite)
    object Profile : NavigationItem(R.string.navigation_item_profile, Icons.Outlined.Person)

}