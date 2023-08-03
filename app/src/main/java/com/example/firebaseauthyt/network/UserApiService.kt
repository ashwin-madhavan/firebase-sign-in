package com.example.firebaseauthyt.network

import com.example.firebaseauthyt.model.MovieReview
import com.example.firebaseauthyt.model.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApiService {

    @POST("users.json")
    suspend fun addUser(@Body user: User): User

    /**
    @GET("users/{userID}.json")
    suspend fun getUser(@Path("userID") userID: String): User

    @PATCH("users/{userID}.json")
    suspend fun updateFriendsList(@Path("userID") userID: String, @Body friendsList: List<String>): User
    **/
}