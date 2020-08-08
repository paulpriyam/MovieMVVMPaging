package com.example.moviemvvmpaging.mvvm.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.moviemvvmpaging.mvvm.model.ResultsItem
import com.example.moviemvvmpaging.mvvm.network.FIRST_PAGE
import com.example.moviemvvmpaging.mvvm.network.IMovieApi
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MovieDataSource(
    private val mIMovieApi: IMovieApi,
    private val mCompositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int , ResultsItem>() {

    private val page = FIRST_PAGE
     val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    override fun loadInitial(
        params: LoadInitialParams<Int >,
        callback: LoadInitialCallback<Int , ResultsItem>
    ) {
        networkState.postValue(NetworkState.LOADING)
        mCompositeDisposable.add(mIMovieApi.getPopularMovies(page).subscribeOn(Schedulers.io())
            .subscribe(
                {
                    callback.onResult(it.results.orEmpty(), null, page + 1)
                    networkState.postValue(NetworkState.LOADED)

                }, {
                    networkState.postValue(NetworkState.ERROR)
                    Log.d("error", "pagination Error=======>${it.message.toString()}")
                }
            ))
    }

    override fun loadAfter(params: LoadParams<Int >, callback: LoadCallback<Int , ResultsItem>) {
        networkState.postValue(NetworkState.LOADING)
        mCompositeDisposable.add(mIMovieApi.getPopularMovies(params.key)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    if (it.totalPages!!>= params.key) {
                        callback.onResult(it.results.orEmpty(), params.key + 1)
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        networkState.postValue(NetworkState.END_OF_LIST)
                    }

                }, {
                    networkState.postValue(NetworkState.ERROR)
                    Log.d("error", "pagination Error=======>${it.message.toString()}")
                }
            ))
    }

    override fun loadBefore(params: LoadParams<Int >, callback: LoadCallback<Int , ResultsItem>) {
        TODO("Not yet implemented")
    }
}