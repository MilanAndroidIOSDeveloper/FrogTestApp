package com.android.frogtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.frogtest.models.Movie
import com.android.frogtest.models.MovieResponse
import com.android.frogtest.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var currentPage = 1
    private var currentYear = 2000;

    init {
        getMovies()
    }

    fun getMovies() {
        if (_isLoading.value == true) return
        _isLoading.value = true

        viewModelScope.launch {
            val response = repository.getMovies(currentPage, currentYear)
            handleResponse(response)
        }
    }

    private fun handleResponse(response: Response<MovieResponse>) {
        if (response.isSuccessful) {
            if (response.body()?.response.equals("False")) {
                currentYear += 1
                currentPage = 1
            } else {
                val newMovies = response.body()?.search ?: emptyList()
                _movies.value = newMovies
                currentPage++
            }
        }
        _isLoading.value = false
    }

    fun getMoreMovies() {
        if (_isLoading.value == true) return
        _isLoading.value = true

        viewModelScope.launch {
            val response = repository.getMovies(currentPage, currentYear)
            handleResponse(response)
        }
    }
}
