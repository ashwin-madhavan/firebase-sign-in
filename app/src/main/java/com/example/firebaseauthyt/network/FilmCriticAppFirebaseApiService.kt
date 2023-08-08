package com.example.firebaseauthyt.network

import com.example.firebaseauthyt.model.Group
import com.example.firebaseauthyt.model.MovieReview
import com.example.firebaseauthyt.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmCriticAppFirebaseApiService {
    @GET("movie-reviews.json?orderBy=\"userID\"")
    suspend fun getMovieReviewsByUserID(@Query("equalTo") id: String): Map<String, MovieReview>

    @POST("movie-reviews.json")
    suspend fun addMovieReview(@Body movieReview: MovieReview): MovieReview

    @GET("users.json?orderBy=\"userID\"")
    suspend fun getUsersByUserID(
        @Query("equalTo") id: String
    ): Map<String, User>

    @GET("groups/{groupID}.json")
    suspend fun getGroupByFirebaseGeneratedGroupID(
        @Path("groupID") groupID: String
    ): Group

    @GET("groups.json?orderBy=\"groupID\"")
    suspend fun getGroupByGroupID(@Query("equalTo") id: Long): Map<String, Group>

    @POST("groups.json")
    suspend fun addGroup(@Body group: Group): Group
}