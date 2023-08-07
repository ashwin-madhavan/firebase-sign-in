package com.example.firebaseauthyt.model

data class Group(
    val groupID: Long,
    val name: String,
    val owner: String,
    val members: List<String>
)
