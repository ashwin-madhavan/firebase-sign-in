package com.example.firebaseauthyt.presentation.movie_details_screen

import android.view.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun MovieDetailsScreen(movieID: Int) {
    Text(text = movieID.toString())
}