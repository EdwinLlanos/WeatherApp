package com.weather.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.weather.app.navigation.ScreenNavigationSpec
import com.weather.app.presentation.search.navigation.SearchNavigationSpec
import com.weather.app.presentation.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                val navController = rememberNavController()
                val startDestination: String = SearchNavigationSpec.route
                Scaffold(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) { innerPadding ->
                    NavHost(
                        navController = navController, startDestination = startDestination,
                        Modifier.padding(innerPadding)
                    ) {
                        ScreenNavigationSpec.allScreens.forEach { screen ->
                            composable(
                                screen.route,
                                screen.arguments,
                                screen.deepLinks,
                            ) { navBackStackEntry ->
                                screen.Content(navController, navBackStackEntry)
                            }
                        }
                    }
                }
            }
        }
    }
}
