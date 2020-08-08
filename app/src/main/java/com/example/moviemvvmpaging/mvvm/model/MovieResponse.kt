package com.example.moviemvvmpaging.mvvm.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
	val title: String? = null,
	val id: Int? = null,
	val overview: String? = null,
	@SerializedName("poster_path") val posterPath: String? = null,
	val tagline: String? = null
)
