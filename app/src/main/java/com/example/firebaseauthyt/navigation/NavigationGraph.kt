package com.example.firebaseauthyt.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.firebaseauthyt.presentation.DatabaseViewModel
import com.example.firebaseauthyt.presentation.add_review_screen.AddReviewScreen
import com.example.firebaseauthyt.presentation.MovieAPIViewModel
import com.example.firebaseauthyt.presentation.group_chats_screen.GroupChatDetailsScreen
import com.example.firebaseauthyt.presentation.group_chats_screen.GroupChatsScreen
import com.example.firebaseauthyt.presentation.home_screen.HomeScreen
import com.example.firebaseauthyt.presentation.login_screen.SignInScreen
import com.example.firebaseauthyt.presentation.manage_friends_screen.ManageFriendsScreen
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
                { navController.navigate(Screens.ManageFriendsScreen.route) },
                { navController.navigate(Screens.GroupChatsScreen.route) },
                { movieId -> navController.navigate("${Screens.MovieDetailsScreen.route}/$movieId") },
                { navController.navigate(Screens.AddReviewScreen.route) })
        }
        composable(route = Screens.ManageFriendsScreen.route) {
            ManageFriendsScreen()
        }
        composable(route = Screens.GroupChatsScreen.route) {
            GroupChatsScreen { groupId ->
                navController.navigate("${Screens.GroupChatDetailsScreen.route}/$groupId")
            }
        }
        composable(
            route = "${Screens.GroupChatDetailsScreen.route}/{groupId}",
            arguments = listOf(navArgument("groupId") { type = NavType.IntType })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val groupId = arguments.getInt("groupId")
            GroupChatDetailsScreen(groupId.toLong())
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
