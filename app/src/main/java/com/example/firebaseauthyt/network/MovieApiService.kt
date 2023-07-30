package com.example.firebaseauthyt.network

import com.example.firebaseauthyt.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("search/movie")
    suspend fun getMovies(@Query("query") query: String): MovieResponse
}