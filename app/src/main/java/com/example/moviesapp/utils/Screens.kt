package com.example.moviesapp.utils

sealed class Screens(val route: String) {
    object Home: Screens("Main")
    object PopularMoviesList: Screens("Popular")
    object UpComingMoviesList: Screens("UpComing")
    object Details: Screens("Details")
}