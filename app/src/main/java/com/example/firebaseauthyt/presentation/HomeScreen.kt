package com.example.firebaseauthyt.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(argument: String?) {
    Surface() {
        Column() {
            Text(text = "We made it to the home-screen!")
            if (argument != null) {
                Text(text = argument)
            }
        }

    }
}
