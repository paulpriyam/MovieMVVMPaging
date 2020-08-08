package com.example.moviemvvmpaging.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviemvvmpaging.mvvm.model.MovieResponse
import com.example.moviemvvmpaging.mvvm.repository.MovieDetailRepository
import com.example.moviemvvmpaging.mvvm.repository.NetworkState
import io.reactivex.rxjava3.disposables.CompositeDisposable

class SingleMovieViewModel(
    private val movieDetailRepository: MovieDetailRepository,
    private val movieId: Int
) : ViewModel() {

    private val mCompositeDisposable = CompositeDisposable()

    val moviedetails: LiveData<MovieResponse> by lazy {
        movieDetailRepository.fetchMovieDetails(mCompositeDisposable, movieId)
    }

    val netowrkState: LiveData<NetworkState> by lazy {
        movieDetailRepository.getMovieDetailNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.dispose()
    }
}