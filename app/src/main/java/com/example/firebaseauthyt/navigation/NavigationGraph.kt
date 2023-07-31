package com.example.firebaseauthyt.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    val homeViewModel: HomeViewModel = HomeViewModel()

    NavHost(
        navController = navController,
        startDestination = Screens.SignInScreen.route
    ) {
        composable(route = Screens.SignInScreen.route) {
            SignInScreen(
                onSignUpClick = { navController.navigate(Screens.SignUpScreen.route) },
                onScreenSignInSuccess = { argumentValue ->
                    navController.navigate(
                        "${
                            Screens.HomeScreenWithArgument.route.replace(
                                "{argument_key}",
                                argumentValue
                            )
                        }"
                    )
                })
        }
        composable(route = Screens.SignUpScreen.route) {
            SignUpScreen()
        }
        var userID: String? = null
        composable(route = Screens.HomeScreenWithArgument.route) { backStackEntry ->
            userID = backStackEntry.arguments?.getString("argument_key")
            homeViewModel.updateUserID(userID)
            Log.d("TEST CHECK", homeViewModel.uiState.value?.userID.toString())
            HomeScreen(userID, homeViewModel) { navController.navigate(Screens.TestScreen.route) }
        }
        composable(route = Screens.TestScreen.route) {
            AddReviewScreen(userID, homeViewModel)
        }
    }

}
