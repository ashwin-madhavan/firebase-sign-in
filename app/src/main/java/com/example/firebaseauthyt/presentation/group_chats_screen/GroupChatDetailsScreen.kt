package com.example.firebaseauthyt.presentation.group_chats_screen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.firebaseauthyt.navigation.Screens

@Composable
fun GroupChatDetailsScreen(groupID: Long) {
    Text(text = groupID.toString())
}