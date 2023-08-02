package com.example.firebaseauthyt.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebaseauthyt.presentation.DatabaseViewModel
import com.example.firebaseauthyt.presentation.add_review_screen.AddReviewScreen
import com.example.firebaseauthyt.presentation.home_screen.HomeScreen
import com.example.firebaseauthyt.presentation.home_screen.HomeState
import com.example.firebaseauthyt.presentation.home_screen.HomeViewModel
import com.example.firebaseauthyt.presentation.login_screen.SignInScreen
import com.example.firebaseauthyt.presentation.signup_screen.SignUpScreen

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
) {

    val databaseViewModel: DatabaseViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = Screens.SignInScreen.route
    ) {
        composable(route = Screens.SignInScreen.route) {
            SignInScreen(
                onSignUpClick = { navController.navigate(Screens.SignUpScreen.route) },
                onScreenSignInSuccess = {
                    navController.navigate(Screens.HomeScreen.route)
                })
        }
        composable(route = Screens.SignUpScreen.route) {
            SignUpScreen()
        }
        composable(route = Screens.HomeScreen.route) {
            HomeScreen(databaseViewModel) { navController.navigate(Screens.TestScreen.route) }
        }
        composable(route = Screens.TestScreen.route) {
            AddReviewScreen(databaseViewModel)
        }
    }

}
