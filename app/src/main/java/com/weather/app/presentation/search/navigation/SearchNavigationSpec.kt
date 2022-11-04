package com.weather.app.presentation.search.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.weather.app.navigation.ScreenNavigationSpec
import com.weather.app.presentation.detail.navigation.DetailNavigationSpec.DETAIL_SCREEN
import com.weather.app.presentation.search.SearchScreen

object SearchNavigationSpec : ScreenNavigationSpec {

    override val route = "search"

    @Composable
    override fun Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry,
    ) {
        SearchScreen { query ->
            navController.navigate("$DETAIL_SCREEN/$query")
        }
    }
}
