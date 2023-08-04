package com.example.firebaseauthyt.presentation.home_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.firebaseauthyt.model.MovieReview
import com.example.firebaseauthyt.presentation.DatabaseViewModel


@Composable
fun HomeScreen(
    viewModel: DatabaseViewModel,
    onManageFriendClicked: () -> Unit,
    onGroupChatsClicked: () -> Unit,
    onItemClick: (Long) -> Unit,
    onAddReviewClicked: () -> Unit
) {
    viewModel.getCurUserMovieReviews()

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
                Button(onClick = onManageFriendClicked) {
                    Text(text = "Manage Friends")
                }
                Button(onClick = onGroupChatsClicked) {
                    Text(text = "Group Chats")
                }
                LazyColumn(
                    contentPadding = PaddingValues(
                        vertical = 8.dp,
                        horizontal = 8.dp
                    )
                ) {
                    items(viewModel.curUserMovieReviewListState.value) { movieReview ->
                        MovieReviewItem(movieReview, onItemClick)
                    }
                }
            }
        }
    )
}

@Composable
fun MovieReviewItem(movieReview: MovieReview, onItemClick: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onItemClick(movieReview.movieID) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            MovieDetails(
                title = movieReview.title,
                review = movieReview.review,
                rating = movieReview.rating,
                modifier = Modifier.weight(0.7f)
            )
        }
    }
}

@Composable
fun MovieDetails(
    title: String,
    review: String,
    rating: Int,
    modifier: Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    Column(modifier = modifier, horizontalAlignment = horizontalAlignment) {
        Row {
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.weight(0.6f)
            )
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(0.4f)
            ) {
                for (i in 0 until rating) {
                    RatingIcon(
                        icon = Icons.Filled.Favorite,
                        modifier = Modifier.padding(horizontal = 4.dp) // Optional: Add some horizontal spacing between icons
                    )
                }
            }

        }
        Text(
            text = review,
            style = MaterialTheme.typography.body1
        )
    }


}

@Composable
fun RatingIcon(icon: ImageVector, modifier: Modifier) {
    Image(
        imageVector = icon,
        contentDescription = "Rating Icon",
    )
}