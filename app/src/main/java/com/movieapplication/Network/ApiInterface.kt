

import com.movieapplication.BuildConfig
import com.movieapplication.model.MovieDetailResponse
import com.movieapplication.model.MovieResponse

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface  ApiInterface {


    @GET("/")
    fun getMovieResponse(@Query("i") t:String,@Query("apiKey")  apikey:String, @Query("page") pageNumber: Int, @Query("s") search : String): Call<MovieResponse>



    @GET("/")
    fun getDetailMovieResponse(@Query("apiKey") apiKey: String, @Query("i") movieId: String): Call<MovieDetailResponse>




    @GET("employees")
    fun getEmp():Call<MovieResponse>

    companion object {
        operator fun invoke(
        ): ApiInterface {

            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .build()
                val request = chain.request()
                    .newBuilder()
                 //   .addHeader("Authorization", "")
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }


            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
            }

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(requestInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl("http://www.omdbapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }
    }

}