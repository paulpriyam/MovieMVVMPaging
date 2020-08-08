package com.example.moviemvvmpaging.mvvm.model

import com.google.gson.annotations.SerializedName

data class PopularMovieResponse(
	val page: Int? = null,
	@SerializedName("total_pages") val totalPages: Int? = null,
	val results: List<ResultsItem?>? = null,
	val totalResults: Int? = null
)
