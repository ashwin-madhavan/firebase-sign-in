package com.example.firebaseauthyt.presentation.group_chats_screen

import android.util.Log
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
    private var groupIDCount: Int = 0


    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://fir-authyt-23f0c-default-rtdb.firebaseio.com/")
            .build()
        restInterface = retrofit.create(FilmCriticAppFirebaseApiService::class.java)

        userLiveData.observeForever { user ->
            curUser = user
            curUser?.let { getGroups(it) }
        }

        val curUserID = firebaseAuth.uid.toString()
        getUser(curUserID)
        fetchGroupIDCount()
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

    private fun getGroups(user: User) {
        val groupIDList: List<Long> = user.groupsList
        val fetchedGroups = mutableListOf<Group>()

        viewModelScope.launch {
            try {
                groupIDList.forEach { groupID ->
                    val group = getRemoteGroups(groupID)
                    fetchedGroups.add(group)
                }

                groupLiveData.postValue(fetchedGroups)
            } catch (e: Exception) {
                Log.e("GROUP RESPONSE3", "Error fetching groups: ${e.message}")
            }
        }
    }

    private suspend fun getRemoteGroups(groupID: Long): Group {
        val response =
            restInterface.getGroupByGroupID(groupID)
        return response.values.first()
    }

    /**
    fun addNewGroup(
    groupID: Long,
    name: String,
    owner: String,
    members: List<String>
    ) {
    viewModelScope.launch() {
    withContext(Dispatchers.Default) { curUser?.let { getGroups(it) } }
    }

    viewModelScope.launch() {
    addRemoteNewGroup(groupID.toLong(), name, owner, members)
    curUser?.let { getGroups(it) }
    }
    }
     **/
    fun addNewGroup(name: String, owner: String, members: List<String>) {
        viewModelScope.launch {
            try {
                addRemoteNewGroup(name, owner, members)
            } catch (e: Exception) {
                Log.e("ADD GROUP", "Error adding new group: ${e.message}")
            }
        }
    }

    private suspend fun addRemoteNewGroup(
        name: String,
        owner: String,
        members: List<String>
    ) {
        return withContext(Dispatchers.IO) {
            val group = Group(
                groupIDCount.toLong(), name, owner, members
            )
            restInterface.addGroup(group)

            // Increment groupIDCount locally
            groupIDCount++

            // Update the GroupIDCount in the database
            updateGroupIDCountInDatabase()
        }
    }


    private suspend fun updateGroupIDCountInDatabase() {
        try {
            val updateValue = mapOf("GroupIDCount" to groupIDCount)
            restInterface.updateGroupIDCount(updateValue)
        } catch (e: Exception) {
            Log.e("GROUP COUNT", "Error updating group count in the database: ${e.message}")
        }
    }

    private fun fetchGroupIDCount() {
        viewModelScope.launch {
            try {
                groupIDCount = restInterface.getGroupIDCount()
            } catch (e: Exception) {
                Log.e("GROUP COUNT", "Error fetching group count: ${e.message}")
            }
        }
    }
}

