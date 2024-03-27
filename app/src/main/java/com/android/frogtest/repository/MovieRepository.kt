package com.android.frogtest.repository

import com.android.frogtest.di.RetrofitInstance
class MovieRepository {
    suspend fun getMovies(page: Int, year: Int) = RetrofitInstance.api.getMovies(page, year)
}