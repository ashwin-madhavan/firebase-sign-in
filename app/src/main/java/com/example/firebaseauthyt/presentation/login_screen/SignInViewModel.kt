package com.example.firebaseauthyt.presentation.login_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseauthyt.data.AuthRepository
import com.example.firebaseauthyt.util.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    val _signInState = Channel<SignInState>()
    val signInState = _signInState.receiveAsFlow()

    val _googleState = mutableStateOf(GoogleSignInState())
    val googleState: State<GoogleSignInState> = _googleState

    private val _uidState = mutableStateOf("")
    var uidState: State<String> by mutableStateOf(_uidState)


    fun googleSignIn(credential: AuthCredential) = viewModelScope.launch {
        repository.googleSignIn(credential).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _googleState.value = GoogleSignInState(success = result.data)
                    // Get the Firebase Auth instance
                    val firebaseAuth = FirebaseAuth.getInstance()

                    // Get the currently signed-in user
                    val currentUser = firebaseAuth.currentUser

                    // Check if the user is not null before accessing the UID
                    if (currentUser != null) {
                        _uidState.value = currentUser.uid
                    }
                }
                is Resource.Loading -> {
                    _googleState.value = GoogleSignInState(loading = true)
                }
                is Resource.Error -> {
                    _googleState.value = GoogleSignInState(error = result.message!!)
                }
            }


        }
    }


    fun loginUser(email: String, password: String) = viewModelScope.launch {
        repository.loginUser(email, password).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _signInState.send(SignInState(isSuccess = "Sign In Success "))

                    val firebaseAuth = FirebaseAuth.getInstance()

                    // Get the currently signed-in user
                    val currentUser = firebaseAuth.currentUser
                    if (currentUser != null) {
                        Log.d("SignIn-View-Model", currentUser.uid.toString())
                    }
                    // Check if the user is not null before accessing the UID
                    if (currentUser != null) {
                        _uidState.value = currentUser.uid
                    }
                }
                is Resource.Loading -> {
                    _signInState.send(SignInState(isLoading = true))
                }
                is Resource.Error -> {
                    _signInState.send(SignInState(isError = result.message))
                }
            }

        }
    }

}