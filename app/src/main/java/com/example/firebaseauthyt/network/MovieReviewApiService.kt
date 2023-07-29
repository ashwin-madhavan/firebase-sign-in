package com.example.firebaseauthyt.network

import com.example.firebaseauthyt.model.MovieReview
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MovieReviewApiService {
    @GET("movie-reviews.json")
    suspend fun getMovieReviews(): Map<String, MovieReview>

    @GET("movie-reviews.json?orderBy=\"r_id\"")
    suspend fun getMovieReview(@Query("equalTo") id: Int): Map<String, MovieReview>

    @POST("movie-reviews.json")
    suspend fun addMovieReview(@Body restaurant: MovieReview): MovieReview

}