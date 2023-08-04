package com.example.firebaseauthyt.presentation.group_chats_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.firebaseauthyt.presentation.DatabaseViewModel
import com.example.firebaseauthyt.presentation.home_screen.MovieReviewItem

@Composable
fun GroupChatsScreen(viewModel: DatabaseViewModel, onItemClick: (Long) -> Unit) {

    val userList = listOf<String>("MMmrz89Bt4fcztqQmf0LTAPMV9W2", "testUserID")
    Column {

        Button(onClick = { viewModel.getGroupChatMovieReview(userList) }) {
            Text(text = "Test")

        }

        LazyColumn(
            contentPadding = PaddingValues(
                vertical = 8.dp,
                horizontal = 8.dp
            )
        ) {
            items(viewModel.groupChatMovieReviewListState.value) { movieReview ->
                MovieReviewItem(movieReview, onItemClick)
            }
        }
    }
}