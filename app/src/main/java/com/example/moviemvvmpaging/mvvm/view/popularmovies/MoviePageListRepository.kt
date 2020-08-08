package com.example.moviemvvmpaging.mvvm.view.popularmovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moviemvvmpaging.mvvm.model.ResultsItem
import com.example.moviemvvmpaging.mvvm.network.IMovieApi
import com.example.moviemvvmpaging.mvvm.network.POST_PER_PAGE
import com.example.moviemvvmpaging.mvvm.repository.MovieDataSource
import com.example.moviemvvmpaging.mvvm.repository.MovieDataSourceFactory
import com.example.moviemvvmpaging.mvvm.repository.NetworkState
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MoviePageListRepository(private val mIMovieApi: IMovieApi) {

    lateinit var mMoviePagedList: LiveData<PagedList<ResultsItem>>
    lateinit var mMovieDataSourceFactory: MovieDataSourceFactory

    fun fetchMoviePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<ResultsItem>> {

        mMovieDataSourceFactory = MovieDataSourceFactory(mIMovieApi, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        mMoviePagedList = LivePagedListBuilder(mMovieDataSourceFactory, config).build()
        return mMoviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {

        return Transformations.switchMap<MovieDataSource, NetworkState>(
            mMovieDataSourceFactory.movieMutableLiveData, MovieDataSource::networkState
        )
    }
}