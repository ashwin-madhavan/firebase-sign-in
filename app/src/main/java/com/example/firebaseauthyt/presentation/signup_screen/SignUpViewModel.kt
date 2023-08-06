package com.example.firebaseauthyt.presentation.signup_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseauthyt.data.AuthRepository
import com.example.firebaseauthyt.model.User
import com.example.firebaseauthyt.network.UserApiService
import com.example.firebaseauthyt.presentation.home_screen.HomeState
import com.example.firebaseauthyt.presentation.login_screen.SignInState
import com.example.firebaseauthyt.util.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val repository: AuthRepository
) : ViewModel() {

    val _signUpState = Channel<SignInState>()
    val signUpState = _signUpState.receiveAsFlow()

    private var restInterface: UserApiService
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
            UserApiService::class.java
        )
        _uiState.value = HomeState()
    }

    fun addUser(user: User) {
        viewModelScope.launch() {
            restInterface.addUser(user)
        }
    }

    fun registerUser(email: String, password: String, name: String) = viewModelScope.launch {
        repository.registerUser(email, password).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _signUpState.send(SignInState(isSuccess = "Sign Up Success "))

                    val testUser = User(
                        userID = firebaseAuth.uid.toString(),
                        name = name,
                        friendsList = listOf(firebaseAuth.uid.toString())
                        ,
                        groupsList = listOf("-Nb2-ZyaVRkGknGv7u4b")
                    )
                    addUser(testUser)
                }

                is Resource.Loading -> {
                    _signUpState.send(SignInState(isLoading = true))
                }

                is Resource.Error -> {

                    _signUpState.send(SignInState(isError = result.message))
                }
            }

        }
    }

}