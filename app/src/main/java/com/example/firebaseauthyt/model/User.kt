package com.example.firebaseauthyt.model

data class User(
    //@SerializedName("is_shutdown")
    val userID: String,
    val name: String,
    val friendsList: List<String>,
    val groupsList: List<Long>

)