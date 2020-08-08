package com.example.moviemvvmpaging.mvvm.repository

import androidx.lifecycle.LiveData
import com.example.moviemvvmpaging.mvvm.model.MovieResponse
import com.example.moviemvvmpaging.mvvm.network.IMovieApi
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MovieDetailRepository(private val mIMovieApi: IMovieApi) {
    lateinit var mMovieDetailDataSource: MovieDetailDataSource

    fun fetchMovieDetails(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<MovieResponse> {
        mMovieDetailDataSource = MovieDetailDataSource(mIMovieApi, compositeDisposable)
        mMovieDetailDataSource.fetchMovieDetail(movieId)
        return mMovieDetailDataSource.moviedetailResponse
    }

    fun getMovieDetailNetworkState(): LiveData<NetworkState> {
        return mMovieDetailDataSource.networkState
    }
}