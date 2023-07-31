package com.example.firebaseauthyt.model

data class MovieReview(
    //@SerializedName("is_shutdown")
    val userID : String,
    val title: String,
    val review: String,
    val rating: Int
)