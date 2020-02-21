
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.movieapplication.model.MovieDetailResponse
import com.movieapplication.model.MovieResponse
import com.weatherapp.Resource


import okhttp3.Headers

class Repository {


    fun getWhetherResponse(search:String): LiveData<Resource<MovieResponse>> {
        val data = MutableLiveData<Resource<MovieResponse>>()

           val call = ApiInterface().getMovieResponse("tt3896198","49265d48",1,search)
        call.enqueue(object : ResponseListener<MovieResponse>() {
            override fun onResponse(t: MovieResponse?, headers: Headers) {
                data.setValue(Resource.Success(t!!))
            }


            override fun onError(message: String) {
                data.setValue(Resource.Error("error"))

            }
        })
        return data
    }

    fun getDetailMovieResponse(movieId:String): LiveData<Resource<MovieDetailResponse>> {
        val data = MutableLiveData<Resource<MovieDetailResponse>>()

        //  val call = ApiInterface().getWetherValues("bangalore", "177a607f441dae2fbc776cabc17e2454")

        val call = ApiInterface().getDetailMovieResponse( "49265d48",movieId)
        call.enqueue(object : ResponseListener<MovieDetailResponse>() {
            override fun onResponse(t: MovieDetailResponse?, headers: Headers) {
                data.setValue(Resource.Success(t!!))
            }


            override fun onError(message: String) {
                data.setValue(Resource.Error("error"))

            }
        })
        return data
    }

}