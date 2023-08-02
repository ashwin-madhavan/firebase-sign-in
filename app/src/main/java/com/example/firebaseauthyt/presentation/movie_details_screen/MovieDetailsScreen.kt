package com.example.firebaseauthyt.presentation.movie_details_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import com.example.firebaseauthyt.model.Movie
import com.example.firebaseauthyt.presentation.MovieAPIViewModel
import com.google.accompanist.imageloading.LoadPainterDefaults

@Composable
fun MovieDetailsScreen(movieID: Long, movieAPIViewModel: MovieAPIViewModel) {
    movieAPIViewModel.getMovie(movieID)
    val movieResponse: Movie? = movieAPIViewModel.uiMovieState
    Column {
        if (movieResponse != null) {
            MovieDetailsItem(movieID = movieResponse)
        }
    }
}

@Composable
fun MovieDetailsItem(movieID: Movie) {
    Surface() {
        Column {
            Text(text = movieID.title + "(" + movieID.release_date + ")")
            Text(text = movieID.overview)

            val imageUrl = "https://image.tmdb.org/t/p/w500" + movieID.poster_path
            Image(
                painter = rememberImagePainter(
                    data = imageUrl,
                    imageLoader = LocalImageLoader.current,
                    builder = {
                        if (false == true) this.crossfade(LoadPainterDefaults.FadeInTransitionDuration)
                        placeholder(0)
                    }
                ),
                contentDescription = null, // Content description for accessibility (can be null for images)
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit, // Use ContentScale options to adjust how the image fits within the composable
            )
        }
    }
}