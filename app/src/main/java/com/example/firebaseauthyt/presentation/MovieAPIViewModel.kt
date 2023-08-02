package com.example.firebaseauthyt.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseauthyt.model.Movie
import com.example.firebaseauthyt.network.MovieApiService
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieAPIViewModel(private val stateHandle: SavedStateHandle) : ViewModel() {

    private var restInterface: MovieApiService
    val state = mutableStateOf(emptyList<Movie>())
    var uiMovieState: Movie? = null
    //mutableStateOf<MovieResponse?>(null)

    init {
        val accessToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2MDI4NTQ2ZDIwMjA3OWM5YzJkZjU2NmIwOWVlODZmOCIsInN1YiI6IjY0YmIzNjg4NThlZmQzMDExYzNlOWM1NSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.on3kpSkegdhzfdl5jpJXGIkQekoI_rPPzcSJOUM7cTE"

        // Create an OkHttpClient with an Interceptor to add the authorization header
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    val originalRequest: Request = chain.request()
                    val newRequest: Request = originalRequest.newBuilder()
                        .header("Authorization", "Bearer $accessToken")
                        .build()
                    return chain.proceed(newRequest)
                }
            })
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.themoviedb.org/3/")
            .client(okHttpClient) // Set the OkHttpClient here
            .build()

        restInterface = retrofit.create(MovieApiService::class.java)
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            try {
                val movieResponse = restInterface.searchForMovie(query)
                // Handle the movieResponse here, which contains the list of movies matching the query
                // For example, you can access the list of movies: movieResponse.results
                state.value = movieResponse.results
            } catch (e: Exception) {
                // Handle error cases here
            }
        }
    }

    fun getMovie(query: Long) {
        viewModelScope.launch {
            try {
                val movieResponse = restInterface.getMovieDetails(query)
                uiMovieState = movieResponse
                Log.d("STATE2", uiMovieState.toString())
            } catch (e: Exception) {
                // TODO
            }
        }
    }
}