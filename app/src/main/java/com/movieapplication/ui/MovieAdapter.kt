package com.movieapplication.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.movieapplication.Utils.MovieListener
import com.movieapplication.R
import com.movieapplication.Utils.RecyclerViewHolder
import com.movieapplication.databinding.LayoutMoviewAppBinding
import com.movieapplication.model.Search
import com.squareup.picasso.Picasso


class MovieAdapter(
    val context: Context, val movielist: List<Search>,var listener: MovieListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecyclerViewHolder(
            DataBindingUtil.inflate<LayoutMoviewAppBinding>(
                LayoutInflater.from(parent.context),
                R.layout.layout_moview_app,
                parent,
                false
            )
        )

    override fun getItemCount() = movielist.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding: LayoutMoviewAppBinding =
            (holder as RecyclerViewHolder<*>).binding as LayoutMoviewAppBinding



        binding.viewModel = movielist[position]

        Picasso.get().load(movielist[position].Poster).into(binding.moviesListItemCoverImage)



        binding.root.setOnClickListener(View.OnClickListener {
            listener.showMovieDetail(movielist[position].imdbID)
        })


        binding.executePendingBindings()
    }

}