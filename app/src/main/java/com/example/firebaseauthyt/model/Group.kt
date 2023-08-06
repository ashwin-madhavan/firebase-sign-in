package com.example.firebaseauthyt.model

data class Group(
    val name: String,
    val owner: String,
    val members: List<String>
)
