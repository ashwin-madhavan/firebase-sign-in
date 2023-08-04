package com.example.firebaseauthyt.presentation.manage_friends_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseauthyt.data.AuthRepository
import com.example.firebaseauthyt.model.User
import com.example.firebaseauthyt.network.UserApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


@HiltViewModel
class ManageFriendsViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val repository: AuthRepository
) : ViewModel() {


    private var restInterface: UserApiService

    val curUserID = firebaseAuth.uid.toString()

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(
                "https://fir-authyt-23f0c-default-rtdb.firebaseio.com/"
            )
            .build()
        restInterface = retrofit.create(
            UserApiService::class.java
        )
    }

    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")

    fun addFriendToUser(userID: String, friendID: String) {
        // Search for the user with the specified userID
        val userQuery: Query = usersRef.orderByChild("userID").equalTo(userID)


        userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check if the user with the specified userID exists
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        // Retrieve the user object
                        val user = userSnapshot.getValue(User::class.java)

                        // Check if the user object is not null
                        if (user != null) {
                            // Get the current friends list
                            val currentFriendsList = user.friendsList.toMutableList()

                            // Add the new friend to the friends list
                            currentFriendsList.add(friendID)

                            // Update the user's friendsList in the database
                            usersRef.child(userSnapshot.key!!).child("friendsList")
                                .setValue(currentFriendsList)
                                .addOnSuccessListener {
                                    println("Friend added successfully.")
                                }
                                .addOnFailureListener {
                                    println("Failed to add friend: ${it.message}")
                                }
                        }
                    }
                } else {
                    Log.d("Database CHECK", "User with userID $userID not found.")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Database CHECK", "Error searching for user: ${databaseError.message}")
            }
        })
    }

    fun getUser() {
        viewModelScope.launch {
            val nameToSearch = "Jack"
            val argument = "\"" + nameToSearch + "\""
            try {
                val response = restInterface.getUsersByName(id = argument)
                Log.e("GET USERS", response.toString())
            } catch (e: Exception) {
                Log.e("GET USERS", "Error fetching users: ${e.message}")
            }
        }
    }

    fun setUserAdmin() {

    }


    /**
    fun addFriendToUser2(friendID: String = "test") {
    viewModelScope.launch() {

    // Get the current user's data from the database
    val user = restInterface.getUser(curUserID)

    // Add the new friend to the current friends list
    val updatedFriendsList = user.friendsList.toMutableList()
    updatedFriendsList.add(friendID)

    // Update the friends list on the server
    restInterface.updateFriendsList(curUserID, updatedFriendsList)
    }
    }
     **/
}