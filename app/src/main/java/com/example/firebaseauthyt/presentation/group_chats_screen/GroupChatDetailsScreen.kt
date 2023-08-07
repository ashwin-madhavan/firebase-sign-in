package com.example.firebaseauthyt.presentation.group_chats_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebaseauthyt.presentation.home_screen.MovieReviewItem

@Composable
fun GroupChatDetailsScreen(groupID: Long, onItemClick: (Long) -> Unit) {
    val viewModel: GroupChatDetailsViewModel = hiltViewModel()
    viewModel.getGroup(groupID)


    Column {
        Text(text = groupID.toString())
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