package com.example.moviemvvmpaging.mvvm.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.moviemvvmpaging.mvvm.model.MovieResponse
import com.example.moviemvvmpaging.mvvm.model.ResultsItem
import com.example.moviemvvmpaging.mvvm.network.IMovieApi
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MovieDataSourceFactory(
    private val mIMovieApi: IMovieApi,
    private val mCompositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, ResultsItem>() {
     val movieMutableLiveData = MutableLiveData<MovieDataSource>()
    override fun create(): DataSource<Int, ResultsItem> {
        val mMovieDataSource = MovieDataSource(mIMovieApi, mCompositeDisposable)
        movieMutableLiveData.postValue(mMovieDataSource)
        return mMovieDataSource
    }
}