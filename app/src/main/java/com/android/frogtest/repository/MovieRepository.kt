package com.android.frogtest.repository

import com.android.frogtest.api.ApiService

class MovieRepository (private val apiService: ApiService) {
    suspend fun getMovies(page: Int, year: Int) = apiService.getMovies(page, year)
}