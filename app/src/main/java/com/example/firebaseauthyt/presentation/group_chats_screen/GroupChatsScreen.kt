package com.example.firebaseauthyt.presentation.group_chats_screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebaseauthyt.model.Group
import com.example.firebaseauthyt.model.User
import com.example.firebaseauthyt.presentation.home_screen.MovieReviewItem

@Composable
fun GroupChatsScreen(viewModel: GroupChatViewModel = hiltViewModel()) {
    val thisCurUser by viewModel.userLiveData.observeAsState()
    val thisCurUserGroups by viewModel.groupLiveData.observeAsState()

    Column {
        val userName = thisCurUser?.name ?: "Loading Name..."
        LazyColumn(
            contentPadding = PaddingValues(
                vertical = 8.dp,
                horizontal = 8.dp
            )
        ) {
            items(thisCurUserGroups ?: emptyList()) { item ->
                GroupCard(group = item)
            }
        }
    }
}

@Composable
fun GroupCard(group: Group) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { /* Handle click here */ },
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = group.name) // Display the group using its toString() representation
            // You can add more information from the group object as needed
        }
    }
}
