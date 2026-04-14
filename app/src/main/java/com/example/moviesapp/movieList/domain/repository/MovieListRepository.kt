package com.example.moviesapp.movieList.domain.repository

import com.example.moviesapp.movieList.domain.model.Movie
import com.example.moviesapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {
    suspend fun getMovieList(
        page: Int,
        category: String,
        forceFetchFromRemote: Boolean
    ): Flow<Resource<List<Movie>>>

    suspend fun getMovie(
        id: Int
    ): Flow<Resource<Movie>>

}