package com.weather.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import com.weather.app.presentation.detail.navigation.DetailNavigationSpec
import com.weather.app.presentation.search.navigation.SearchNavigationSpec

interface ScreenNavigationSpec {

    companion object {
        val allScreens = listOf(
            SearchNavigationSpec,
            DetailNavigationSpec
        )
    }

    val route: String
    val arguments: List<NamedNavArgument> get() = emptyList()
    val deepLinks: List<NavDeepLink> get() = emptyList()

    @Composable
    fun Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    )
}
