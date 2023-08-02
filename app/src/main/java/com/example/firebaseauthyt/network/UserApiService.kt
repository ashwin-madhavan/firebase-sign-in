package com.example.firebaseauthyt.network

import com.example.firebaseauthyt.model.MovieReview
import com.example.firebaseauthyt.model.User
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {
    @POST("users.json")
    suspend fun addUser(@Body user: User): User
}