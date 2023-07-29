package com.example.firebaseauthyt.presentation.home_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.firebaseauthyt.model.MovieReview


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(userID: String?) {
    val viewModel: HomeViewModel = HomeViewModel()
    Surface(onClick = { /*TODO*/ }) {
        Column() {


            Button(onClick = { viewModel.addMovieReview("test", "test-title", "test-review", 5) }) {
                Text(text = "Test Button")
            }

            LazyColumn(
                contentPadding = PaddingValues(
                    vertical = 8.dp,
                    horizontal = 8.dp
                )
            ) {
                items(viewModel.movieReviewListState.value) { movieReview ->
                    MovieReviewItem(movieReview)
                }
            }
        }
    }
}

@Composable
fun MovieReviewItem(movieReview: MovieReview) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Title: " + movieReview.title)
            Text(text = "Review: " + movieReview.review)
            Text(text = "Rating: " + movieReview.rating)
        }
    }
}