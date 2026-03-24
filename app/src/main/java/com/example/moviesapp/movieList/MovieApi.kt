package com.example.moviesapp.movieList

import com.example.moviesapp.movieList.respond.MovieListDTO
import com.google.gson.internal.GsonBuildConfig
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.moviesapp.BuildConfig

interface MovieApi {
    @GET("movie/{category}")

    suspend fun getMovieList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") api_key: String = API_KEY
    ): MovieListDTO

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/"
        const val API_KEY = BuildConfig.MOVIE_API_KEY
    }

}