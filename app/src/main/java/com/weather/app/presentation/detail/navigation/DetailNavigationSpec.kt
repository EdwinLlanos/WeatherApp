package com.weather.app.presentation.detail.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.weather.app.navigation.ScreenNavigationSpec
import com.weather.app.presentation.detail.DetailScree

object DetailNavigationSpec : ScreenNavigationSpec {

    const val DETAIL_SCREEN = "detailScreen"
    private const val QUERY_PARAM = "query"
    override val route = "$DETAIL_SCREEN/{$QUERY_PARAM}"

    override val arguments = listOf(
        navArgument(QUERY_PARAM) {
            type = NavType.StringType
        }
    )

    @Composable
    override fun Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry,
    ) {
        val query = navBackStackEntry.arguments?.getString(QUERY_PARAM)
        DetailScree(query = query) {
            navController.popBackStack()
        }
    }
}
