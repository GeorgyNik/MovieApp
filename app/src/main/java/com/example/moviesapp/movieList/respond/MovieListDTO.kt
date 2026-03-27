package com.example.moviesapp.movieList.respond

data class MovieListDTO(
    val page: Int,
    val result: List<MovieDTO>,
    val totalPages: Int,
    val totalResults: Int
)
