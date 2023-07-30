package com.example.firebaseauthyt.presentation.home_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.firebaseauthyt.model.MovieReview


@Composable
fun HomeScreen(userID: String?, onAddReviewClicked: () -> Unit) {
    val viewModel: HomeViewModel = HomeViewModel()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddReviewClicked() },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Review")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column() {


                    Button(onClick = {
                        viewModel.addMovieReview(
                            "test",
                            "test-title",
                            "test-review",
                            5
                        )
                    }) {
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
    )
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