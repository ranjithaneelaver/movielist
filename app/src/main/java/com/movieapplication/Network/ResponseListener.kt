
import com.google.gson.Gson
import okhttp3.Headers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection



    abstract class ResponseListener<T> : Callback<T> {

        protected abstract fun onResponse(t: T?, headers: Headers)

        protected abstract fun onError(message: String)

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                onResponse(response.body(), response.headers())
            } else {
                if (response.code() == HttpURLConnection.HTTP_BAD_REQUEST) {
                    try {
                        val errors = Gson().fromJson<Errors>(
                            response.errorBody()!!.string(),
                            Errors::class.java!!
                        )
                     //   onError(errors.getError().getMessage())
                        return
                    } catch (e: IOException) {
                        // e.printStackTrace();
                    }

                }
                onFailure(call, Throwable())
            }

        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            if (t is IOException) {
                onError("Please check your network connection and try again")
            } else {
                onError("Something went wrong, try again later")
            }
        }
    }

