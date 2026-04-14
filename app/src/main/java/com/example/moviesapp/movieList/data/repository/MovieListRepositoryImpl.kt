package com.example.moviesapp.movieList.data.repository

import coil.network.HttpException
import com.example.moviesapp.movieList.MovieApi
import com.example.moviesapp.movieList.data.local.movie.MovieDataBase
import com.example.moviesapp.movieList.data.mappers.toMovie
import com.example.moviesapp.movieList.data.mappers.toMovieEntity
import com.example.moviesapp.movieList.domain.model.Movie
import com.example.moviesapp.movieList.domain.repository.MovieListRepository
import com.example.moviesapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject


class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val db: MovieDataBase
) : MovieListRepository {
    override suspend fun getMovieList(
        page: Int,
        category: String,
        forceFetchFromRemote: Boolean
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            val localMovieList = db.movieDAO.getMovieByCategory(category)

            //проверка необходимости загрузки из кэша
            val shouldLoadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote

            if (shouldLoadLocalMovie) {
                emit(
                    Resource.Success(
                        data = localMovieList.map { movieEntity ->
                            movieEntity.toMovie(category)
                        }
                    ))
                emit(Resource.Loading(false))
                return@flow
            }

            val movieListFromApi = try {
                movieApi.getMovieList(category, page)
            } catch (
                e: IOException //проблемы с сетью
            ) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (
                e: HttpException //проблемы с подключением к серверу или неправильный синтаксис подключения
            ) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error connection to server"))
                return@flow
            } catch (
                e: Exception //любые другие ошибки
            ) {
                e.printStackTrace()
                emit(Resource.Error(message = "Other Error"))
                return@flow
            }

            val movieEntities = movieListFromApi.result.let {
                it.map { movieDTO ->
                    movieDTO.toMovieEntity(category)
                }
            }

            db.movieDAO.upsertMovieList(movieEntities)
            emit(Resource.Success(movieEntities.map { it.toMovie(category) }))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))

            val movieEntity = db.movieDAO.getMovieById(id)
            if (movieEntity != null) {
                emit(Resource.Success(movieEntity.toMovie(movieEntity.category)))
                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error(message = "Not such movie"))
            emit(Resource.Loading(false))
        }
    }
}