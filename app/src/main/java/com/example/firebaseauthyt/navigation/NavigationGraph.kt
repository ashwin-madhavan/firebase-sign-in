package com.example.firebaseauthyt.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.firebaseauthyt.presentation.DatabaseViewModel
import com.example.firebaseauthyt.presentation.add_review_screen.AddReviewScreen
import com.example.firebaseauthyt.presentation.MovieAPIViewModel
import com.example.firebaseauthyt.presentation.home_screen.HomeScreen
import com.example.firebaseauthyt.presentation.login_screen.SignInScreen
import com.example.firebaseauthyt.presentation.movie_details_screen.MovieDetailsScreen
import com.example.firebaseauthyt.presentation.signup_screen.SignUpScreen

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
) {
    val databaseViewModel: DatabaseViewModel = viewModel()
    val movieAPIViewModel: MovieAPIViewModel = viewModel()
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
            HomeScreen(
                databaseViewModel,
                { movieId -> navController.navigate("${Screens.MovieDetailsScreen.route}/$movieId") },
                { navController.navigate(Screens.AddReviewScreen.route) })
        }
        composable(
            route = "${Screens.MovieDetailsScreen.route}/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val movieId = arguments.getInt("movieId")
            MovieDetailsScreen(movieId.toLong(), movieAPIViewModel)
        }
        composable(route = Screens.AddReviewScreen.route) {
            AddReviewScreen(databaseViewModel, movieAPIViewModel)
        }
    }

}
