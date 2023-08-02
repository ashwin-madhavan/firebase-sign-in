package com.example.firebaseauthyt.navigation

sealed class Screens(val route: String) {
    object SignInScreen : Screens(route = "SignIn_Screen")
    object SignUpScreen : Screens(route = "SignUp_Screen")
    object HomeScreen : Screens(route = "Home_Screen")
    object AddReviewScreen : Screens(route = "Add_Review_Screen")
    object MovieDetailsScreen: Screens(route = "Movie_Details_Screen")
}