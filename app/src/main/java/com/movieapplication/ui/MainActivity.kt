package com.movieapplication.ui


import android.content.Context
import android.net.*
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.movieapplication.R
import com.movieapplication.Utils.MovieListener
import com.movieapplication.databinding.ActivityMainBinding
import com.movieapplication.model.MovieResponse
import com.movieapplication.viewmodel.MovieViewModel
import com.weatherapp.Resource
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MovieListener {
    private lateinit var binding: ActivityMainBinding
    private var viewModel: MovieViewModel =
        MovieViewModel()

    private lateinit var movieDetailFragment: MovieDetailFragment
    private lateinit var mFragmentManager: androidx.fragment.app.FragmentManager
    private lateinit var transaction: androidx.fragment.app.FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.viewmodel = viewModel;

        mFragmentManager = supportFragmentManager

        startMovieList("bad")
        binding.moviesListSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length >= 3) {
                    startMovieList(s.toString())

                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Type At Least 3 charecters",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

    }


    override fun showMovieDetail(movieId: String) {
        movie_detail_fragment_container.visibility = View.VISIBLE
        transaction = mFragmentManager.beginTransaction()

        movieDetailFragment =
            MovieDetailFragment.newInstance(movieId)
        transaction.addToBackStack(movieDetailFragment.tag)
        transaction.replace(R.id.movie_detail_fragment_container, movieDetailFragment)
        transaction.commit()
    }

    private val responseObserver = object : Observer<Resource<MovieResponse>> {
        override fun onChanged(it: Resource<MovieResponse>?) {
            when (it?.status) {
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    if (it.data?.Search != null)
                        it.data?.let { it1 -> startAdapter(it1) }
                }

                Resource.Status.ERROR -> {

                    binding.progressBar.visibility = View.GONE
                }

                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }

    }

    fun startMovieList(search: String) {

        if (isInternetAvailable()) {
            viewModel.getResponse(search).observe(this, responseObserver)
        } else {

            Toast.makeText(this,"Check your internet connections",Toast.LENGTH_SHORT).show()

        }

    }

    private fun startAdapter(data: MovieResponse) {
        binding.moviesList.adapter =
            MovieAdapter(this@MainActivity, data.Search, this)

        binding.moviesListSearch.clearFocus()
        val `in` = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        `in`.hideSoftInputFromWindow(binding.moviesListSearch.getWindowToken(), 0)


    }

    private fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.let { cm ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        else -> false
                    }
                }
            } else {
                connectivityManager.activeNetworkInfo.also {
                    result = it != null && it.isConnected
                }
            }
        }
        return result
    }

}
