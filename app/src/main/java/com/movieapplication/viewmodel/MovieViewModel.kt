package com.movieapplication.viewmodel

import Repository
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.movieapplication.model.MovieDetailResponse
import com.movieapplication.model.MovieResponse
import com.weatherapp.Resource


class MovieViewModel : ViewModel() {
    private val repository: Repository = Repository()
    private lateinit var  movieResponse : LiveData<Resource<MovieResponse>>
    private lateinit var detailMovieResponse: LiveData<Resource<MovieDetailResponse>>
     var loading = ObservableField<Boolean>()

   // private lateinit var  todayResponse : LiveData<Resource<MovieResponse>>
    init {
        loading.set(false)
    }

    fun getResponse(search:String): LiveData<Resource<MovieResponse>> {
        loading.set(true)
        movieResponse = repository.getWhetherResponse(search)

        return  movieResponse
    }

    fun getDetailMovie(movieId:String):LiveData<Resource<MovieDetailResponse>>{
        detailMovieResponse = repository.getDetailMovieResponse(movieId)
        return  detailMovieResponse

    }

}