package com.example.moviesapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.example.moviesapp.movieList.presentation.MovieListUIEvent
import com.example.moviesapp.utils.Screens

@Composable
fun BottomNavigationBar(
    onEvent: (MovieListUIEvent) -> Unit,
    navController: NavHostController
) {
    val items = listOf(
        BottomItem(
            title = "Popular",
            icon = Icons.Rounded.Movie
        ),
        BottomItem(
            title = "UpComing",
            icon = Icons.Rounded.Upcoming
        )
    )
    val selected = rememberSaveable { mutableIntStateOf(0) }

    NavigationBar() {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selected.intValue == index,
                    onClick = {
                        selected.intValue = index
                        when (selected.intValue) {
                            0 -> {
                                onEvent(MovieListUIEvent.Navigate)
                                navController.popBackStack()
                                navController.navigate(Screens.PopularMoviesList.route)
                            }

                            1 -> {
                                onEvent(MovieListUIEvent.Navigate)
                                navController.popBackStack()
                                navController.navigate(Screens.UpComingMoviesList.route)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
            }
        }
    }
}

data class BottomItem(
    val title: String,
    val icon: ImageVector
)