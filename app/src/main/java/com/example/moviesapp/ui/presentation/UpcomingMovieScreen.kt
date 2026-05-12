package com.example.moviesapp.ui.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.moviesapp.movieList.presentation.MovieListState
import com.example.moviesapp.movieList.presentation.MovieListUIEvent
import com.example.moviesapp.ui.components.MovieItem
import com.example.moviesapp.utils.Category

@Composable
fun UpcomingMovieScreen(
    movieListState: MovieListState,
    navController: NavHostController,
    onEvent: (MovieListUIEvent) -> Unit
) {
    if (movieListState.upcomingMovieList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                vertical = 8.dp,
                horizontal = 4.dp
            )
        ) {
            items(movieListState.upcomingMovieList.size) { index ->
                MovieItem(
                    movieListState.upcomingMovieList[index],
                    navController
                )
                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                )
                if (index >= movieListState.upcomingMovieList.size-1 && !movieListState.isLoading) {
                    onEvent(MovieListUIEvent.Paginate(Category.UPCOMING))
                }
            }
        }
    }
}