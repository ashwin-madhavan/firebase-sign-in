package com.example.firebaseauthyt.presentation.group_chats_screen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseauthyt.model.Group
import com.example.firebaseauthyt.model.MovieReview
import com.example.firebaseauthyt.model.User
import com.example.firebaseauthyt.network.FilmCriticAppFirebaseApiService
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
class GroupChatDetailsViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) :
    ViewModel() {
    private var restInterface: FilmCriticAppFirebaseApiService

    private val _uiState = MutableLiveData<HomeState>()
    val uiState: LiveData<HomeState> get() = _uiState

    val userLiveData: MutableLiveData<User> = MutableLiveData()
    var curUser: User? = null
    val groupLiveData: MutableLiveData<Group> = MutableLiveData()
    var curGroup: Group? = null
    val groupChatMovieReviewListState: MutableState<List<MovieReview>> =
        mutableStateOf(emptyList<MovieReview>())

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://fir-authyt-23f0c-default-rtdb.firebaseio.com/")
            .build()
        restInterface = retrofit.create(FilmCriticAppFirebaseApiService::class.java)

        userLiveData.observeForever { user ->
            curUser = user
        }

        groupLiveData.observeForever { group ->
            curGroup = group
            if (curGroup != null) {
                getGroupChatMovieReview(curGroup!!.members)
                Log.d("Movie Reviews1", curGroup!!.members.toString())
            }
        }

        val curUserID = firebaseAuth.uid.toString()
        getUser(curUserID)

    }

    private fun getUser(userID: String) {
        viewModelScope.launch {
            try {
                val remoteUserMap = getRemoteUser(userID)
                val user = remoteUserMap.values.first()
                userLiveData.postValue(user)
                _uiState.value = HomeState()
            } catch (e: Exception) {
                Log.e("USER RESPONSE", e.toString())
            }
        }
    }

    private suspend fun getRemoteUser(userID: String): Map<String, User> {
        return withContext(Dispatchers.IO) {
            val argument = "\"$userID\""
            restInterface.getUsersByUserID(id = argument)
        }
    }

    fun getGroup(groupID: Long) {
        viewModelScope.launch {
            try {
                val remoteGroupMap = getRemoteGroup(groupID)
                val group = remoteGroupMap.values.first()
                groupLiveData.postValue(group)
            } catch (e: Exception) {
                Log.e("GROUP RESPONSE", "Error fetching group: ${e.message}")
            }
        }
    }

    private suspend fun getRemoteGroup(groupID: Long): Map<String, Group> {
        return withContext(Dispatchers.IO) {
            restInterface.getGroupByGroupID(groupID)
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

}

