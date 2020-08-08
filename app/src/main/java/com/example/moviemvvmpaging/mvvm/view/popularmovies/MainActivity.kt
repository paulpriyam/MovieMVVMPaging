@file:Suppress("UNCHECKED_CAST")

package com.example.moviemvvmpaging.mvvm.view.popularmovies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviemvvmpaging.databinding.ActivityMainBinding
import com.example.moviemvvmpaging.mvvm.network.IMovieApi
import com.example.moviemvvmpaging.mvvm.network.MovieDbClient
import com.example.moviemvvmpaging.mvvm.repository.NetworkState
import com.example.moviemvvmpaging.mvvm.view.MovieDetail

class MainActivity : AppCompatActivity() {

    private lateinit var mActivityMainBinding: ActivityMainBinding
    private lateinit var mViewModel: MainActivityViewModel
    private lateinit var repository: MoviePageListRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mActivityMainBinding.root)

        val apiService: IMovieApi = MovieDbClient.getClient()
        repository = MoviePageListRepository(apiService)

        mViewModel = MainActivityViewModel(repository)

        val movieAdapter = PopularMoviePagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType: Int = movieAdapter.getItemViewType(position)
                return if (viewType == movieAdapter.MOVIE_VIEW_TYPE) 1 else 2
            }
        }

        mActivityMainBinding.rvRecycler.layoutManager = gridLayoutManager
        mActivityMainBinding.rvRecycler.setHasFixedSize(true)
        mActivityMainBinding.rvRecycler.adapter = movieAdapter

        mViewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })
        mViewModel.networkState.observe(this, Observer {
            mActivityMainBinding.pbProgress.visibility =
                if (mViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            mActivityMainBinding.tvErrorText.visibility =
                if (mViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
            if (!mViewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })

    }


}