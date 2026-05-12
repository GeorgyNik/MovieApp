package com.example.moviesapp.ui.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviesapp.movieList.presentation.MovieListState
import com.example.moviesapp.movieList.presentation.MovieListViewModel
import com.example.moviesapp.ui.components.BottomNavigationBar
import com.example.moviesapp.utils.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movieListState = movieListViewModel.movieListState.collectAsState().value
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                onEvent = movieListViewModel::onEvent,
                navController = bottomNavController
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (movieListState.isCurrentPopularScreen) {
                            "Popular Movies"
                        } else {
                            "Upcoming Movies"
                        },
                        fontSize = 20.sp
                    )
                },
                modifier = Modifier
                    .shadow(elevation = 2.dp),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
            )
        }

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                bottomNavController,
                startDestination = Screens.PopularMoviesList.route
            ) {
                composable(Screens.PopularMoviesList.route) {
                    PopularMovieScreen(
                        movieListState = movieListState,
                        navController = navController,
                        onEvent = movieListViewModel::onEvent
                    )
                }
                composable(Screens.UpComingMoviesList.route) {
                    UpcomingMovieScreen(
                        movieListState = movieListState,
                        navController = navController,
                        onEvent = movieListViewModel::onEvent
                    )
                }
            }
        }
    }
}