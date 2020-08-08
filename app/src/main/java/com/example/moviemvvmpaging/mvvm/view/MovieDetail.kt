package com.example.moviemvvmpaging.mvvm.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.moviemvvmpaging.databinding.ActivityMovieDetailBinding
import com.example.moviemvvmpaging.mvvm.model.MovieResponse
import com.example.moviemvvmpaging.mvvm.network.IMAGE_BASE_URL
import com.example.moviemvvmpaging.mvvm.network.IMovieApi
import com.example.moviemvvmpaging.mvvm.network.MovieDbClient
import com.example.moviemvvmpaging.mvvm.repository.MovieDetailRepository
import com.example.moviemvvmpaging.mvvm.repository.NetworkState
import com.example.moviemvvmpaging.mvvm.viewmodel.SingleMovieViewModel

class MovieDetail : AppCompatActivity() {
    private lateinit var mViewModel: SingleMovieViewModel
    private lateinit var movieDetailRepository: MovieDetailRepository

    private lateinit var mActivityMovieDetailBinding: ActivityMovieDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityMovieDetailBinding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(mActivityMovieDetailBinding.root)

        val movieId: Int = intent.getIntExtra("id", 1)
        val apiService: IMovieApi = MovieDbClient.getClient()
        movieDetailRepository = MovieDetailRepository(apiService)

        mViewModel = getViewModel(movieId)
        mViewModel.moviedetails.observe(this, Observer {
            bindUI(it)
        })

        mViewModel.netowrkState.observe(this, Observer {
            mActivityMovieDetailBinding.pbProgress.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            mActivityMovieDetailBinding.tvErrorText.visibility =
                if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    fun bindUI(movie: MovieResponse) {
        mActivityMovieDetailBinding.run {
            tvMovieTitle.text = movie.title.orEmpty()
            tvMovieTagLine.text = movie.tagline.orEmpty()
            tvMovieInfo.text = movie.overview.orEmpty()

            val moviePosterUrl: String = IMAGE_BASE_URL + movie.posterPath
            Glide.with(this@MovieDetail).load(moviePosterUrl).into(ivMovieImage)

        }
    }

    private fun getViewModel(movieId: Int): SingleMovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SingleMovieViewModel(movieDetailRepository, movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}