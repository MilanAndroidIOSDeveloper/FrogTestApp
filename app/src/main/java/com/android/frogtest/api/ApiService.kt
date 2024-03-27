package com.android.frogtest.api


import com.android.frogtest.models.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

	@GET("?")
	suspend fun getMovies(
		@Query("page") page: Int,
		@Query("y") year: Int,
		@Query("s") query: String = "love",
		@Query("type") type: String = "movie",
		@Query("apikey") apiKey: String = "c254d7e0"
	): Response<MovieResponse>


}