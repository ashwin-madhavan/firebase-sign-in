package com.example.firebaseauthyt.presentation.group_chats_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebaseauthyt.model.Group

@Composable
fun GroupChatsScreen(onItemClick: (Long) -> Unit) {
    val viewModel: GroupChatViewModel = hiltViewModel()
    val thisCurUser by viewModel.userLiveData.observeAsState()
    val thisCurUserGroups by viewModel.groupLiveData.observeAsState()

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Create Group Chat") },
            text = {
                Column {
                    val options = listOf("Option 1", "Option 2", "Option 3")
                    CreateGroupForm(options = options)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addNewGroup(6, "test", "test", listOf("john"))
                        showDialog = false
                    }
                ) {
                    Text("Create Group")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val userName = thisCurUser?.name ?: "Loading Name..."
                Text(
                    text = "Group Chats",
                    style = MaterialTheme.typography.h5,
                    textDecoration = TextDecoration.Underline
                )
                LazyColumn(
                    contentPadding = PaddingValues(
                        vertical = 8.dp,
                        horizontal = 8.dp
                    )
                ) {
                    items(thisCurUserGroups ?: emptyList()) { item ->
                        GroupCard(group = item, onItemClick)
                    }
                }
            }
        }
    )
}

@Composable
fun GroupCard(group: Group, onItemClick: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(group.groupID) },
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = group.name)
        }
    }
}

@Composable
fun CreateGroupForm(options: List<String>) {
    val selectedOptions = remember { mutableStateListOf<String>() }
    var newTitle by remember { mutableStateOf("") }

    Column {
        TextField(
            value = newTitle,
            onValueChange = { newTitle = it },
            label = { Text("Group Name") }
        )

        Text(text = "Select Friends:")

        options.forEach { option ->
            Checkbox(
                checked = selectedOptions.contains(option),
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        selectedOptions.add(option)
                    } else {
                        selectedOptions.remove(option)
                    }
                }
            )
            Text(text = option)
        }

        Text("Selected Friends: ${selectedOptions.joinToString(", ")}")
    }
}





