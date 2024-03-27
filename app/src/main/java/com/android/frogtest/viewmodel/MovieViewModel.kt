package com.android.frogtest.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.frogtest.FrogTestApplication
import com.android.frogtest.R
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
    private var currentYear = 2000

    init {
        getMovies()
    }

    private fun getMovies() {
        if (_isLoading.value == true) return
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getMovies(currentPage, currentYear)
                handleResponse(response)
            } catch (e: Exception) {
                handleErrorResponse(e)
            }
        }
    }

    private fun handleResponse(response: Response<MovieResponse>) {
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.response.equals("False")) {
                currentYear += 1
                currentPage = 1
            } else {
                val newMovies = body?.search ?: emptyList()
                _movies.value = newMovies
                currentPage++
            }
        } else {
            handleErrorResponse(Exception(response.message()))
        }
        _isLoading.value = false
    }

    private fun handleErrorResponse(error: Throwable) {
        val errorMessage = FrogTestApplication.appContext.getString(R.string.error, error.message)
        Toast.makeText(FrogTestApplication.appContext, errorMessage, Toast.LENGTH_SHORT).show()
        _isLoading.value = false
    }

    fun getMoreMovies() {
        if (_isLoading.value == true) return
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getMovies(currentPage, currentYear)
                handleResponse(response)
            } catch (e: Exception) {
                handleErrorResponse(e)
            }
        }
    }
}