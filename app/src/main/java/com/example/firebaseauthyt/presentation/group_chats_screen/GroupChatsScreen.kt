package com.example.firebaseauthyt.presentation.group_chats_screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebaseauthyt.model.User

@Composable
fun GroupChatsScreen(viewModel: GroupChatViewModel = hiltViewModel()) {

    val thisCurUser by viewModel.userLiveData.observeAsState()

    Column {
        if (thisCurUser != null) {
            Text(text = thisCurUser!!.name)
        }
        Text(text = "Hello")
    }
}
