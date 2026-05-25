package com.example.moviesapp.ui.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.movieList.domain.repository.MovieListRepository
import com.example.moviesapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val movieListRepository: MovieListRepository,
    savedStateHandle: SavedStateHandle //получает аргументы навигации и сохраняет состояние при пересоздании ViewModel
) :
    ViewModel() {
    private val movieId: Int = savedStateHandle.get<Int>("movieId") ?: -1
    private val _detailsState = MutableStateFlow(DetailsState())
    val detailsState = _detailsState.asStateFlow()

    fun getMovie(movieId: Int) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovie(movieId).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _detailsState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _detailsState.update {
                            it.copy(isLoading = result.loading)
                        }
                    }

                    is Resource.Success -> {
                        _detailsState.update {
                            it.copy(
                                isLoading = false,
                                movie = result.data
                            )
                        }
                    }
                }
            }
        }
    }
}