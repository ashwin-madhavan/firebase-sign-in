package com.example.firebaseauthyt.network

import com.example.firebaseauthyt.model.MovieReview
import com.example.firebaseauthyt.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {

    @POST("users.json")
    suspend fun addUser(@Body user: User): User

    @GET("users.json?orderBy=\"name\"")
    suspend fun getUsersByName(
        @Query("equalTo") id: String
    ): Map<String, User>

    @GET("users.json?orderBy=\"name\"")
    suspend fun getUsersByName2(
        @Query("equalTo") name: String
    ): Map<String, User>


    /**
    @PATCH("users/{userID}.json")
    suspend fun updateFriendsList(@Path("userID") userID: String, @Body friendsList: List<String>): User
     **/
}