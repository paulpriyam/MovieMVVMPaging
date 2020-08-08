package com.example.moviemvvmpaging.mvvm.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviemvvmpaging.mvvm.model.MovieResponse
import com.example.moviemvvmpaging.mvvm.network.IMovieApi
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Exception

class MovieDetailDataSource(
    private val mIMovieApi: IMovieApi,
    private val mCompositeDisposable: CompositeDisposable
) {

    private val mNetworkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = mNetworkState

    private val mMovieDetailResponse = MutableLiveData<MovieResponse>()
    val moviedetailResponse: LiveData<MovieResponse>
        get() = mMovieDetailResponse

    fun fetchMovieDetail(movieId: Int) {
        mNetworkState.postValue(NetworkState.LOADING)
        try {
            mCompositeDisposable.add(
                mIMovieApi.getMovieDetails(movieId).subscribeOn(Schedulers.io()).subscribe(
                    {
                        mMovieDetailResponse.postValue(it)
                        mNetworkState.postValue(NetworkState.LOADED)
                    }, {
                        mNetworkState.postValue(NetworkState.ERROR)
                        Log.d("error", "networkCallError====>${it.message.toString()}")
                    }
                )
            )
        } catch (e: Exception) {
            Log.d("error", "networkError===>${e.message.toString()}")
        }
    }
}