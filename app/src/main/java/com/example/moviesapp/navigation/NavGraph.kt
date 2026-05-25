package com.example.moviesapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviesapp.ui.presentation.HomeScreen
import com.example.moviesapp.ui.presentation.details.DetailScreen
import com.example.moviesapp.utils.Screens

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        composable(Screens.Home.route) {
            HomeScreen(navController)
        }
        composable(
            route = Screens.Details.route + "/{movieId}",
            arguments = listOf(navArgument(name = "movieId") {
                type = NavType.IntType
            })
        ) {
            DetailScreen()
        }
    }
}