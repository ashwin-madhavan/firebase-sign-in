package com.example.firebaseauthyt.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebaseauthyt.presentation.home_screen.HomeScreen
import com.example.firebaseauthyt.presentation.login_screen.SignInScreen
import com.example.firebaseauthyt.presentation.signup_screen.SignUpScreen

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
) {
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
        composable(route = Screens.HomeScreenWithArgument.route) { backStackEntry ->
            val argument = backStackEntry.arguments?.getString("argument_key")
            HomeScreen(argument)
        }
    }

}
