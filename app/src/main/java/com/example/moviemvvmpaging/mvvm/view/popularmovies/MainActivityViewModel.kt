package com.example.moviemvvmpaging.mvvm.view.popularmovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.moviemvvmpaging.mvvm.model.ResultsItem
import com.example.moviemvvmpaging.mvvm.repository.NetworkState
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainActivityViewModel(private val repo: MoviePageListRepository) : ViewModel() {

    private val mCompositeDisposable = CompositeDisposable()

    val moviePagedList: LiveData<PagedList<ResultsItem>> by lazy {
        repo.fetchMoviePagedList(mCompositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        repo.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.dispose()
    }
}