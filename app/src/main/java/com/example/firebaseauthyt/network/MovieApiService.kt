package com.example.firebaseauthyt.network

import com.example.firebaseauthyt.model.Movie
import com.example.firebaseauthyt.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Long): Movie

    @GET("search/movie")
    suspend fun searchForMovie(@Query("query") query: String): MovieResponse
}