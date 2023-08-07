package com.example.firebaseauthyt.presentation.group_chats_screen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebaseauthyt.navigation.Screens

@Composable
fun GroupChatDetailsScreen(groupID: Long) {
    val viewModel: GroupChatDetailsViewModel = hiltViewModel()
    viewModel.getGroup(groupID)
    Text(text = groupID.toString())
}