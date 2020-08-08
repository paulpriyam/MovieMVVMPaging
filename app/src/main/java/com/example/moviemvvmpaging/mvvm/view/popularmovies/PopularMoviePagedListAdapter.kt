package com.example.moviemvvmpaging.mvvm.view.popularmovies

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviemvvmpaging.R
import com.example.moviemvvmpaging.mvvm.model.ResultsItem
import com.example.moviemvvmpaging.mvvm.network.IMAGE_BASE_URL
import com.example.moviemvvmpaging.mvvm.repository.NetworkState
import com.example.moviemvvmpaging.mvvm.view.MovieDetail
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_movie_detail.view.*
import kotlinx.android.synthetic.main.activity_movie_detail.view.tv_movie_title
import kotlinx.android.synthetic.main.network_state.view.*
import kotlinx.android.synthetic.main.popular_movie_item.view.*

class PopularMoviePagedListAdapter(val context: Context) :
    PagedListAdapter<ResultsItem, RecyclerView.ViewHolder>(MovieDiffCallBack()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2
    private var networkState: NetworkState? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        return if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.popular_movie_item, parent, false)
            MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state, parent, false)
            NetworkStateViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateViewHolder).bind(networkState)
        }
    }

    class MovieDiffCallBack : DiffUtil.ItemCallback<ResultsItem>() {
        override fun areItemsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
            return oldItem == newItem
        }

    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1)
            NETWORK_VIEW_TYPE
        else MOVIE_VIEW_TYPE
    }

    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: ResultsItem?, context: Context) {
            itemView.tv_movie_title.text = movie?.title.orEmpty()
            itemView.tv_movie_release_date.text = movie?.releaseDate.orEmpty()

            val moviePosterUrl: String = IMAGE_BASE_URL + movie?.posterPath
            Glide.with(itemView.context).load(moviePosterUrl).into(itemView.iv_popular_movie)

            itemView.iv_popular_movie.setOnClickListener {
                val intent = Intent(context, MovieDetail::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.pb_progress_pagination.visibility = View.VISIBLE
                Thread.sleep(1000)
            } else {
                itemView.pb_progress_pagination.visibility = View.GONE
            }
            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.tv_error_pagination.visibility = View.VISIBLE
                itemView.tv_error_pagination.text = networkState.msg
            } else {
                itemView.tv_error_pagination.visibility = View.GONE
            }
            if (networkState != null && networkState == NetworkState.END_OF_LIST) {
                itemView.tv_error_pagination.visibility = View.VISIBLE
                itemView.tv_error_pagination.text = networkState.msg
            } else {
                itemView.tv_error_pagination.visibility = View.GONE
            }


        }
    }

    fun setNetworkState(networkState: NetworkState?) {
        val previousState: NetworkState? = this.networkState
        val hadExtraRow: Boolean = hasExtraRow()
        this.networkState = networkState
        val hasExtraRow: Boolean = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != networkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}