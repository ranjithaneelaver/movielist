package com.movieapplication.ui

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.movieapplication.R
import com.movieapplication.databinding.MovieDetailFragmentBinding
import com.movieapplication.viewmodel.MovieViewModel
import com.squareup.picasso.Picasso

class MovieDetailFragment : androidx.fragment.app.Fragment(), LifecycleOwner {

    private lateinit var binding: MovieDetailFragmentBinding
    private lateinit var activity: Activity
    private lateinit var mContext: Context
    private lateinit var movieViewModel: MovieViewModel

    companion object {

        fun newInstance(movieId: String): MovieDetailFragment {
            val movieDetailFragment = MovieDetailFragment()
            val args = Bundle()
            args.putString("movie_id", movieId)
            movieDetailFragment.arguments = args
            return movieDetailFragment
        }
    }

    private lateinit var viewModel: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.movie_detail_fragment, null, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        val movieId: String? = arguments!!.getString(getString(R.string.movie_detail_fragment_arg))
        if(isInternetAvailable()) {
            viewModel.getDetailMovie(movieId!!).observe(viewLifecycleOwner, Observer {
                binding.progressBar.visibility = View.GONE
                binding.viewModel = it.data

                Picasso.get().load(it.data?.Poster).into(binding.image)
            })

        }else{
            Toast.makeText(getActivity(),"Check your internet connections", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        activity = context as Activity
    }


    private fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
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
