package com.example.firebaseauthyt.presentation.manage_friends_screen

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firebaseauthyt.navigation.Screens
import com.example.firebaseauthyt.presentation.signup_screen.SignUpViewModel

@Composable
fun ManageFriendsScreen(viewModel: ManageFriendsViewModel = hiltViewModel()) {
    Column {


        Text(text = "Friends Screen")
        var titleEntered by remember {
            mutableStateOf("")
        }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.8.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(15.dp)
                ),
            value = titleEntered,
            onValueChange = {
                titleEntered = it
            },

            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
        )

        Button(onClick = {
            viewModel.addFriendToUser(
                "eNXGljXtNpYTY9GrslNa1wTgqWG2",
                "test"
            )
        }) {
            Text(text = "Add Friend Test")
        }
    }
}