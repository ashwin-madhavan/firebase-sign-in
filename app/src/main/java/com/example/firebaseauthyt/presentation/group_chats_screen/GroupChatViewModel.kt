package com.example.firebaseauthyt.presentation.group_chats_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseauthyt.model.Group
import com.example.firebaseauthyt.model.User
import com.example.firebaseauthyt.network.FilmCriticAppFirebaseApiService
import com.example.firebaseauthyt.presentation.home_screen.HomeState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class GroupChatViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth) : ViewModel() {
    private var restInterface: FilmCriticAppFirebaseApiService

    private val _uiState = MutableLiveData<HomeState>()
    val uiState: LiveData<HomeState> get() = _uiState

    val userLiveData: MutableLiveData<User> = MutableLiveData()
    val groupLiveData: MutableLiveData<List<Group>> = MutableLiveData()
    var curUser: User? = null

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://fir-authyt-23f0c-default-rtdb.firebaseio.com/")
            .build()
        restInterface = retrofit.create(FilmCriticAppFirebaseApiService::class.java)

        userLiveData.observeForever { user ->
            curUser = user
            curUser?.let { getGroup(it) }
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
                Log.e("User Object", e.toString())
            }
        }
    }

    private suspend fun getRemoteUser(userID: String): Map<String, User> {
        return withContext(Dispatchers.IO) {
            val argument = "\"$userID\""
            restInterface.getUsersByUserID(id = argument)
        }
    }

    private fun getGroup(user: User) {
        val groupIDList: List<String> = user.groupsList
        val fetchedGroups = mutableListOf<Group>()

        viewModelScope.launch {
            try {
                groupIDList.forEach { groupID ->
                    val group = getRemoteGroup(groupID)
                    fetchedGroups.add(group)
                }

                groupLiveData.postValue(fetchedGroups)
            } catch (e: Exception) {
                Log.e("GROUP RESPONSE3", "Error fetching groups: ${e.message}")
            }
        }
    }


    private suspend fun getRemoteGroup(groupID: String): Group {
        return withContext(Dispatchers.IO) {
            restInterface.getGroupByGroupID(groupID)
        }
    }
}

