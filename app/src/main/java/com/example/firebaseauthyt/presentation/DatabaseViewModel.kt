package com.example.firebaseauthyt.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseauthyt.model.MovieReview
import com.example.firebaseauthyt.network.MovieReviewApiService
import com.example.firebaseauthyt.presentation.home_screen.HomeState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


@HiltViewModel
class DatabaseViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth) : ViewModel() {
    val curUserMovieReviewListState: MutableState<List<MovieReview>> =
        mutableStateOf(emptyList<MovieReview>())
    val groupChatMovieReviewListState: MutableState<List<MovieReview>> =
        mutableStateOf(emptyList<MovieReview>())
    private var restInterface: MovieReviewApiService

    private val _uiState = MutableLiveData<HomeState>()
    val uiState: LiveData<HomeState> get() = _uiState
    val curUserID = firebaseAuth.uid.toString()

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

    fun getCurUserMovieReviews() {
        viewModelScope.launch() {
            val movieReviews =
                getRemoteMovieReviews(curUserID) //  Here is where the curUserID is passed in!
            curUserMovieReviewListState.value = movieReviews.values.toList()
        }
    }

    fun getGroupChatMovieReview(userList: List<String>) {
        var totalList: MutableList<MovieReview> = mutableListOf()

        viewModelScope.launch {
            val deferredMovieReviews = userList.map { user ->
                async {
                    getRemoteMovieReviews(user)
                }
            }

            deferredMovieReviews.awaitAll().forEach { userReviews ->
                totalList.addAll(userReviews.values)
            }

            groupChatMovieReviewListState.value = totalList
            Log.d("GROUP CHAT", totalList.toString())
        }
    }

    private suspend fun getRemoteMovieReviews(userID: String): Map<String, MovieReview> {
        return withContext(Dispatchers.IO) {
            val argument = "\"" + userID + "\""
            restInterface.getMovieReviewsByUserID(argument)
        }
    }

    fun addMovieReview(movieID: Long, title: String, review: String, rating: Int) {
        Log.d("ADD REVIEW", "check1");
        if (title.isEmpty() || review.isEmpty()) {
            throw IllegalArgumentException("Title and description cannot be empty")
        }

        viewModelScope.launch() {
            withContext(Dispatchers.Default) { getCurUserMovieReviews() }
        }

        val movieReviewList = curUserMovieReviewListState.value.toList()


        viewModelScope.launch() {
            addRemoteMovieReview(movieID, title, review, rating)
            getCurUserMovieReviews()
        }
    }

    private suspend fun addRemoteMovieReview(
        movieID: Long,
        title: String,
        review: String,
        rating: Int
    ) {
        return withContext(Dispatchers.IO) {
            val movieReview = MovieReview(
                curUserID,
                movieID,
                title,
                review,
                rating
            )
            restInterface.addMovieReview(movieReview)
        }
    }
}