package com.example.firebaseauthyt.presentation.home_screen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseauthyt.model.MovieReview
import com.example.firebaseauthyt.network.MovieReviewApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModel() : ViewModel() {
    val movieReviewListState: MutableState<List<MovieReview>> =
        mutableStateOf(emptyList<MovieReview>())
    private var restInterface: MovieReviewApiService

    private val _uiState = MutableLiveData<HomeState>()
    val uiState: LiveData<HomeState> get() = _uiState

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(
                "https://fir-authyt-23f0c-default-rtdb.firebaseio.com/"
            )
            .build()
        restInterface = retrofit.create(
            MovieReviewApiService::class.java
        )
        _uiState.value = HomeState()
    }

    fun updateUserID(userID: String?) {
        _uiState.value = userID?.let { HomeState(userID = it) }
    }

    fun getMovieReviews() {
        viewModelScope.launch() {
            val movieReviews = getRemoteMovieReviews("testUserID")
            movieReviewListState.value = movieReviews.values.toList()
        }
    }

    private suspend fun getRemoteMovieReviews(userID: String): Map<String, MovieReview> {
        return withContext(Dispatchers.IO) {
            val argument = "\"" + userID + "\""
            restInterface.getMovieReviewsByUserID(argument)
        }
    }


    fun addMovieReview(userID: String, title: String, review: String, rating: Int) {
        Log.d("ADD REVIEW", "check1");
        if (title.isEmpty() || review.isEmpty()) {
            throw IllegalArgumentException("Title and description cannot be empty")
        }

        viewModelScope.launch() {
            withContext(Dispatchers.Default) { getMovieReviews() }
        }
        Log.d("ADD REVIEW", "check2");

        val movieReviewList = movieReviewListState.value.toList()
        /**
        val containsDuplicate = movieReviewList.none { it.title == title }
        Log.d("TAG", movieReviewList.toString())
        Log.d("TAG", containsDuplicate.toString())
        if (!containsDuplicate) {
        throw IllegalArgumentException("The title '$title' already exists.")
        }
         **/

        viewModelScope.launch() {
            addRemoteMovieReview(userID, title, review, rating)
            getMovieReviews()
        }
    }

    private suspend fun addRemoteMovieReview(
        userID: String,
        title: String,
        review: String,
        rating: Int
    ) {
        return withContext(Dispatchers.IO) {
            val movieReview = MovieReview(
                userID,
                title,
                review,
                rating
            )
            restInterface.addMovieReview(movieReview)
        }
    }
}