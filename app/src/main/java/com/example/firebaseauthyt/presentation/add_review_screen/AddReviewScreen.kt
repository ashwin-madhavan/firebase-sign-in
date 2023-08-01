package com.example.firebaseauthyt.presentation.add_review_screen

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firebaseauthyt.model.Movie
import com.example.firebaseauthyt.presentation.home_screen.HomeViewModel


@SuppressLint("UnrememberedMutableState")
@Composable
fun AddReviewScreen(userID: String?, homeViewModel: HomeViewModel) {
    val context = LocalContext.current
    val viewModel: SearchMovieViewModel = viewModel()

    var moviesList by remember { mutableStateOf(viewModel.state.value) }
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    var titleEntered by remember {
        mutableStateOf("")
    }


    val heightTextFields by remember {
        mutableStateOf(55.dp)
    }
    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }
    var expanded by remember {
        mutableStateOf(false)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }

    // Category Field
    Column(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    expanded = false
                }
            )
    ) {

        Text(
            modifier = Modifier.padding(start = 3.dp, bottom = 2.dp),
            text = "Search for Movies",
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )

        Column(modifier = Modifier.fillMaxWidth()) {

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(heightTextFields)
                        .border(
                            width = 1.8.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .onGloballyPositioned { coordinates ->
                            textFieldSize = coordinates.size.toSize()
                        },
                    value = titleEntered,
                    onValueChange = {
                        titleEntered = it
                        viewModel.searchMovies(titleEntered)
                        moviesList = viewModel.state.value
                        Log.d("Tag", "check")
                        expanded = true
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black
                    ),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = "arrow",
                                tint = Color.Black
                            )
                        }
                    }
                )
            }

            AnimatedVisibility(visible = expanded) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .width(textFieldSize.width.dp),
                    elevation = 15.dp,
                    shape = RoundedCornerShape(10.dp)
                ) {

                    LazyColumn(
                        modifier = Modifier.heightIn(max = 150.dp),
                    ) {

                        if (titleEntered.isNotEmpty()) {
                            items(
                                moviesList

                            ) {
                                CategoryItems(movie = it) { movie ->
                                    titleEntered = movie.title
                                    selectedMovie = movie
                                    expanded = false
                                }
                            }
                        } else {
                            items(
                                moviesList
                            ) {
                                CategoryItems(
                                    movie = it,
                                    onSelect = { movie ->
                                        titleEntered = movie.title
                                        selectedMovie = movie
                                        expanded = false
                                    }
                                )
                            }
                        }

                    }

                }
            }

            selectedMovie?.let { displayMovieInfo(movie = selectedMovie!!) }

            var review by remember {
                mutableStateOf("")
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        border = BorderStroke(2.dp, Color.Black),
                        shape = MaterialTheme.shapes.medium
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    TextField(value = review, onValueChange = { review = it })

                    var rating: Int = 1
                    SimpleRadioButtonComponent { selectedRating ->
                        rating = selectedRating
                    }

                    Button(onClick = {
                        if (userID != null) {
                            selectedMovie?.let {
                                homeViewModel.addMovieReview(
                                    userID, it.title, review, rating
                                )
                            }
                        } else {
                            /*TODO*/
                        }
                        Toast.makeText(context, "Added!", Toast.LENGTH_LONG).show()
                    }) {
                        Text(text = "Add Review")
                    }
                }

            }
        }
    }
}

@Composable
fun displayMovieInfo(movie: Movie) {
    Text(text = movie.id.toString())
    Text(text = movie.title)
    Text(text = movie.release_date)
    Text(text = movie.overview)
}

@Composable
fun CategoryItems(
    movie: Movie,
    onSelect: (Movie) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelect(movie)
            }
            .padding(10.dp)
    ) {
        val releaseYear = movie.release_date.substring(0, 4)
        Text(text = movie.title + " (" + releaseYear + ")", fontSize = 16.sp)
    }

}

@Composable
fun SimpleRadioButtonComponent(onRatingSelected: (Int) -> Unit) {
    val radioOptions = listOf(1, 2, 3, 4, 5)
    val (selectedOption, setSelectedOption) = remember { mutableStateOf(radioOptions[2]) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        radioOptions.forEach { option ->
            Column(
                modifier = Modifier
                    .selectable(
                        selected = (option == selectedOption),
                        onClick = { setSelectedOption(option) }
                    )
            ) {
                Text(
                    text = option.toString(),
                    modifier = Modifier.padding(start = 16.dp)
                )

                RadioButton(
                    selected = (option == selectedOption),
                    onClick = {
                        setSelectedOption(option)

                        // Call the callback to update the rating variable
                        onRatingSelected(option)
                    },
                )
            }
        }
    }
}
















