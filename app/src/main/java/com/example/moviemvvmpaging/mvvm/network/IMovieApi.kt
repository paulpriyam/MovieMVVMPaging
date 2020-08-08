package com.example.moviemvvmpaging.mvvm.network

import com.example.moviemvvmpaging.mvvm.model.MovieResponse
import com.example.moviemvvmpaging.mvvm.model.PopularMovieResponse
import com.example.moviemvvmpaging.mvvm.repository.NetworkState
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IMovieApi {

//    https://api.themoviedb.org/3/movie/495764?api_key=1d80287db777c3758fe022e5285b3ef0
//    https://api.themoviedb.org/3/movie/popular?api_key=1d80287db777c3758fe022e5285b3ef0&page=1

    @GET("movie/{movieId}")
    fun getMovieDetails(@Path("movieId") movieId: Int): Single<MovieResponse>

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Single<PopularMovieResponse>

}