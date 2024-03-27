package com.android.frogtest.models

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("Search")
    val search: List<Movie>,
    @SerializedName("totalResults")
    val totalResults: Int,
    @SerializedName("Response")
    val response: String
)

