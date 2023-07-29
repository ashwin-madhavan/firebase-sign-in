package com.example.firebaseauthyt.presentation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.core.content.pm.ShortcutInfoCompat.Surface

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TestScreen() {
    Surface(onClick = { /*TODO*/ }) {
        Text(text = "test")
    }
}