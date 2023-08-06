package com.example.firebaseauthyt.presentation.group_chats_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebaseauthyt.model.User

@Composable
fun GroupChatsScreen(viewModel: GroupChatViewModel = hiltViewModel()) {
    Column {
        if (viewModel.curUser != null) {
            Text(text = viewModel.curUser!!.name)
            Text(text = "Hello")
        }
        Text(text = "Hello")
    }
}
