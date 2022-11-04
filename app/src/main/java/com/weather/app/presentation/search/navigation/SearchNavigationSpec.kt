package com.weather.app.presentation.search.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.weather.app.navigation.ScreenNavigationSpec
import com.weather.app.presentation.search.SearchScreen

object StoreNavigationSpec : ScreenNavigationSpec {

    override val route = "search"

    @Composable
    override fun Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry,
    ) {
        SearchScreen()
        // navController.navigate(LibrarySectionsScreenNavigationSpec.route)
    }
}
